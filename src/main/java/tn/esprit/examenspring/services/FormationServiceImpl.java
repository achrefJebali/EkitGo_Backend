package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.entities.Formation;

import java.util.List;


@Service
@Slf4j
public class FormationServiceImpl implements  IFormationService {

    @Autowired
private FormationRepository formationRepository;

    @Override
    public Formation addFormation(Formation formation) {
        return formationRepository.save(formation);
    }

    @Override
    public List<Formation> getFormations() {
        return formationRepository.findAll();
    }

    @Override
    public Formation modifyFormation(Formation formation) {
         formationRepository.save(formation);
        return formation;
    }

    @Override
    public void deleteFormation(Integer id) {
        formationRepository.deleteById(id);


    }
}
