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
    public Optional<Formation> getFormationById(Integer id) {
        return formationRepository.findById(id);
    }
    @Override
    public Formation modifyFormation(Integer idFormation, Formation formation) {
        Formation existingFormation = formationRepository.findById(idFormation)
                .orElseThrow(() -> new RuntimeException("Formation not found with id: " + idFormation));

        existingFormation.setTitle(formation.getTitle());
        existingFormation.setDescription(formation.getDescription());
        existingFormation.setPrice(formation.getPrice());
        existingFormation.setImage(formation.getImage());
        existingFormation.setVideo(formation.getVideo());
        existingFormation.setLabel(formation.getLabel());
        existingFormation.setDuration(formation.getDuration());
        existingFormation.setCertificate(formation.getCertificate());
        existingFormation.setDiscount(formation.getDiscount());
        existingFormation.setFeatured(formation.getFeatured());
        existingFormation.setHighestRated(formation.getHighestRated());
        existingFormation.setProgression(formation.getProgression());
        return formationRepository.save(existingFormation);
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

    @Override
    public Formation retrieveFormation(Integer idFormation) {
        log.info("Fetching formation with ID: {}", idFormation);
        Formation formation = formationRepository.findByIdWithCategory(idFormation)
                .orElseThrow(() -> new IllegalArgumentException("Formation with ID " + idFormation + " not found"));
        log.info("Fetched formation with category: {}", formation.getCategory());
        return formation;
    }


}
