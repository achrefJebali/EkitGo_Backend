package tn.esprit.examenspring.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tn.esprit.examenspring.entities.Formation;

import java.util.List;
import java.util.Optional;

public interface IFormationService {

    Formation addFormation(Formation formation);

    // Updated to support pagination
    Page<Formation> getFormations(Pageable pageable);

    Formation modifyFormation(Integer id, Formation formation);

    void deleteFormation(Integer id);

    Formation affecterCategoryAFormationByName(Integer idFormation, String categoryName);

    Formation retrieveFormation(Integer idFormation);

    // Updated to support pagination
    Page<Formation> searchFormations(String title, String categoryName, Float minPrice, Float maxPrice, String label, Pageable pageable);

    Optional<Formation> getFormationById(Integer id);

    Formation updateDiscount(Integer id, Integer discount);

    Formation findByTitle(String title);
    List<Formation> retrieveAllFormations();
}