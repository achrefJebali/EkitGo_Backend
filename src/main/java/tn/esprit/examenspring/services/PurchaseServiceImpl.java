package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.PurchaseRepository;
import tn.esprit.examenspring.Repository.UserRepository;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.entities.Purchase;
import tn.esprit.examenspring.entities.User;
import tn.esprit.examenspring.entities.Formation;
import tn.esprit.examenspring.services.EmailService;
import tn.esprit.examenspring.services.IProgressService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PurchaseServiceImpl implements IPurchaseService {

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private IProgressService progressService;

    @Autowired
    private EmailService emailService;

    @Override
    public Purchase createPurchase(Integer userId, Integer formationId, String paymentReference) {
        log.info("Creating purchase for userId: {}, formationId: {}", userId, formationId);
        // Check if user has already purchased this formation
        if (hasPurchasedFormation(userId, formationId)) {
            log.warn("User {} has already purchased formation {}", userId, formationId);
            throw new RuntimeException("User has already purchased this formation");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Formation formation = formationRepository.findById(formationId)
                .orElseThrow(() -> new RuntimeException("Formation not found with ID: " + formationId));

        Purchase purchase = new Purchase();
        purchase.setUser(user);
        purchase.setFormation(formation);
        purchase.setPurchaseDate(new Date());
        purchase.setPaymentReference(paymentReference);
        purchase.setActive(true);

        Purchase savedPurchase = purchaseRepository.save(purchase);
        log.info("Purchase created successfully for userId: {}, formationId: {}", userId, formationId);

        // Send purchase confirmation email
        if (savedPurchase.getUser() != null && savedPurchase.getUser().getEmail() != null && savedPurchase.getFormation() != null) {
            String studentName = savedPurchase.getUser().getName();
            String email = savedPurchase.getUser().getEmail();
            String formationName = savedPurchase.getFormation().getTitle();
            emailService.sendPurchaseConfirmation(email, studentName, formationName);
        }

        // Create initial Progress record
        progressService.createOrUpdateProgress(userId, formationId, 0, false, null);
        log.info("Initial progress record created for userId: {}, formationId: {}", userId, formationId);

        return savedPurchase;
    }

    @Override
    public List<Purchase> getPurchasedFormations(Integer userId) {
        log.info("Fetching purchased formations for userId: {}", userId);
        return purchaseRepository.findByUserId(userId);
    }

    @Override
    public boolean hasPurchasedFormation(Integer userId, Integer formationId) {
        log.info("Checking if userId: {} has purchased formationId: {}", userId, formationId);
        List<Purchase> purchases = purchaseRepository.findAllByUserIdAndFormationId(userId, formationId);
        boolean hasActivePurchase = purchases.stream().anyMatch(Purchase::isActive);
        if (purchases.size() > 1) {
            log.warn("Multiple purchase records found for userId: {}, formationId: {}. Found {} records.", userId, formationId, purchases.size());
        }
        return hasActivePurchase;
    }
}