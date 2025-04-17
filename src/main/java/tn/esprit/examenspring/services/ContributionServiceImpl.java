package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.ContributionRepository;
import tn.esprit.examenspring.entities.Contribution;

import java.util.List;
@Service
@Slf4j

public class ContributionServiceImpl implements  IContributionService {


        @Autowired
        private ContributionRepository contributionRepository; // Injection correcte du repository

        @Override
        public Contribution addContribution(Contribution contribution) {
            return contributionRepository.save(contribution);
        }

        @Override
        public List<Contribution> getContributions() {
            return contributionRepository.findAll();
        }

        @Override
        public Contribution modifyContribution(Contribution contribution) {
            return contributionRepository.save(contribution);
        }

        @Override
        public void deleteContribution(Integer id) {
            contributionRepository.deleteById(id);
        }
}
