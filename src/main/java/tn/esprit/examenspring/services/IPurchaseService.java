package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Purchase;

import java.util.List;

public interface IPurchaseService {
    Purchase createPurchase(Integer userId, Integer formationId, String paymentReference);
    List<Purchase> getPurchasedFormations(Integer userId);
    boolean hasPurchasedFormation(Integer userId, Integer formationId);

}