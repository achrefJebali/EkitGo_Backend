package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Formation;

import java.util.List;

public interface IFormationService {

Formation addFormation(Formation formation);
    List<Formation> getFormations();
    Formation modifyFormation(Formation formation);
    void deleteFormation(Integer id);
}
