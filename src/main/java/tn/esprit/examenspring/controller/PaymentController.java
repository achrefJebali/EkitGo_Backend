package tn.esprit.examenspring.controller;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Purchase;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.services.IPurchaseService;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.services.PaymentService;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private IPurchaseService purchaseService; // Inject PurchaseService

    @Autowired
    private UserRepository userRepository; // Inject UserRepository

    @Value("${stripe.webhook.secret}")
    private String webhookSecret; // Add this to application.properties

    @PostMapping("/create-session")
    public ResponseEntity<String> createPaymentSession(
            @RequestParam Integer formationId,
            @RequestParam String studentEmail,
            @RequestParam Integer userId) { // Ensure userId is received
        try {
            System.out.println("Received payment request with: formationId=" + formationId + ", studentEmail=" + studentEmail + ", userId=" + userId);
            String paymentUrl = paymentService.createPaymentSession(formationId, studentEmail, userId);
            return ResponseEntity.ok(paymentUrl);
        } catch (com.stripe.exception.StripeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            // Verify the webhook signature
            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);

            // Process the event
            switch (event.getType()) {
                case "checkout.session.completed":
                    // Get the Session object from the event data
                    Session session = (Session) event.getDataObjectDeserializer().getObject().orElseThrow(() ->
                            new RuntimeException("Failed to deserialize Stripe event data"));

                    // Access customer email
                    String customerEmail = session.getCustomerEmail();
                    if (customerEmail == null) {
                        // Fallback: Try to get email from metadata
                        Map<String, String> metadata = session.getMetadata();
                        customerEmail = metadata != null ? metadata.get("customerEmail") : null;
                        if (customerEmail == null) {
                            throw new RuntimeException("Customer email not found in session or metadata");
                        }
                    }

                    // Access payment intent ID
                    String paymentIntentId = session.getPaymentIntent();
                    if (paymentIntentId == null) {
                        throw new RuntimeException("Payment intent not found in session");
                    }

                    // Find the user by email
                    User user = userRepository.findByEmail(customerEmail);
                    if (user == null) {
                        throw new RuntimeException("User not found for email: " + customerEmail);
                    }

                    // Extract userId and formationId from session metadata
                    Map<String, String> metadata = session.getMetadata();
                    String userIdStr = metadata != null ? metadata.get("userId") : null;
                    String formationIdStr = metadata != null ? metadata.get("formationId") : null;

                    if (userIdStr == null || formationIdStr == null) {
                        throw new RuntimeException("User ID or Formation ID not found in session metadata");
                    }

                    Integer userId = Integer.valueOf(userIdStr);
                    Integer formationId = Integer.valueOf(formationIdStr);

                    // Record the purchase
                    Purchase purchase = purchaseService.createPurchase(userId, formationId, paymentIntentId);
                    log.info("Purchase recorded for user {} and formation {}", userId, formationId);

                    // Redirect to frontend with userId and formationId
                    String sessionId = session.getId(); // Get the session ID
                    String redirectUrl = "http://localhost:4200/payment-success?session_id=" + sessionId + "&userId=" + userId + "&formationId=" + formationId;
                    return ResponseEntity.ok(redirectUrl); // Return the redirect URL as a response

                default:
                    log.info("Unhandled event type: " + event.getType());
            }

            return ResponseEntity.ok("Webhook received");
        } catch (SignatureVerificationException e) {
            // Invalid signature
            return ResponseEntity.status(400).body("Invalid signature");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Webhook error: " + e.getMessage());
        }
    }

    // Add the missing endpoint for recording purchases
    @PostMapping("/purchase/{userId}/{formationId}")
    public ResponseEntity<Purchase> createPurchase(
            @PathVariable Integer userId,
            @PathVariable Integer formationId,
            @RequestParam String paymentReference) {
        try {
            System.out.println("Recording purchase with: userId=" + userId + ", formationId=" + formationId + ", paymentReference=" + paymentReference);
            Purchase purchase = purchaseService.createPurchase(userId, formationId, paymentReference);
            return ResponseEntity.ok(purchase);
        } catch (Exception e) {
            log.error("Failed to record purchase: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}