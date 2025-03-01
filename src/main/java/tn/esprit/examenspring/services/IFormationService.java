package tn.esprit.examenspring.services;

import jakarta.persistence.criteria.CriteriaBuilder;
import tn.esprit.examenspring.entities.Formation;

import java.util.List;

public interface IFormationService {

Formation addFormation(Formation formation);
    List<Formation> getFormations();
    Formation modifyFormation(Integer id,Formation formation);
    void deleteFormation(Integer id);
    Formation affecterCategoryAFormationByName(Integer idFormation, String categoryName);
    Formation retrieveFormation(Integer idFormation);

}
