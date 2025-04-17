package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Contribution;

import java.util.List;

public interface IContributionService {
    Contribution addContribution(Contribution contribution);
    List<Contribution> getContributions();
    Contribution modifyContribution(Contribution contribution);
    void deleteContribution(Integer id);
}
