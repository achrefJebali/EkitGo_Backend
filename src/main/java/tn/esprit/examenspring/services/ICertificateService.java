package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Certificate;

public interface ICertificateService {
    Certificate issueCertificate(Integer userId, Integer formationId);
    Certificate getCertificate(Integer userId, Integer formationId);
    boolean canIssueCertificate(Integer userId, Integer formationId);
    byte[] generateCertificatePdf(Integer userId, Integer formationId) throws Exception;
}