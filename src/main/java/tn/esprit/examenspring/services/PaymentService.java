package tn.esprit.examenspring.services;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.Repository.FormationRepository;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class PaymentService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Autowired
    private FormationRepository formationRepository;


    @PostConstruct
    public void init() {
        System.out.println("Stripe Secret Key: " + stripeSecretKey);
        Stripe.apiKey = stripeSecretKey;
    }

    public String createPaymentSession(Integer formationId, String studentEmail, Integer userId) throws StripeException {
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        System.out.println("Creating payment session with: formationId=" + formationId + ", studentEmail=" + studentEmail + ", userId=" + userId);

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/payment-success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/payment-cancel")
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd")
                                                .setUnitAmount((long) (formation.getDiscountedPrice() * 100)) // Use discounted price
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName(formation.getTitle())
                                                                .build()
                                                )
                                                .build()
                                )
                                .setQuantity(1L)
                                .build()
                )
                .setCustomerEmail(studentEmail) // Set customer email directly
                .putMetadata("formationId", formationId.toString()) // Add formationId to metadata
                .putMetadata("userId", userId.toString()) // Add userId to metadata
                .putMetadata("customerEmail", studentEmail) // Add customer email to metadata as fallback
                .build();

        Session session = Session.create(params);
        System.out.println("Created session with URL: " + session.getUrl());
        return session.getUrl();
    }
}