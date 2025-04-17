package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.CategoryRepository;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.entities.Category;
import tn.esprit.examenspring.entities.Formation;

import java.util.*;


@Service
@Slf4j
public class FormationServiceImpl implements  IFormationService {

    @Autowired
private FormationRepository formationRepository;
    @Autowired
    private CategoryRepository categoryRepository;

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

    @Override
    public Formation affecterCategoryAFormationByName(Integer id, String categoryName) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation non trouvée"));

        // Récupérer la Category par son nom
        Category category = categoryRepository.findCategoriesByname(categoryName);
        if (category == null) {
            throw new RuntimeException("Category non trouvée");
        }
        // Affecter la Category à la Formation
        formation.setCategory(category);

        // Sauvegarder la Formation mise à jour
        return formationRepository.save(formation);
    }
}
