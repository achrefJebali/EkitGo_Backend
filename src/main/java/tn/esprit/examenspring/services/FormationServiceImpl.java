package tn.esprit.examenspring.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.CategoryRepository;
import tn.esprit.examenspring.Repository.FormationRepository;
import tn.esprit.examenspring.entities.Category;
import tn.esprit.examenspring.entities.Formation;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class FormationServiceImpl implements IFormationService {

    @Autowired
    private FormationRepository formationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Formation addFormation(Formation formation) {
        // Calculate discounted price
        if (formation.getPrice() != null && formation.getDiscount() != null && formation.getDiscount() > 0) {
            float discounted = formation.getPrice() - (formation.getPrice() * formation.getDiscount() / 100f);
            formation.setDiscountedPrice(discounted);
        } else {
            formation.setDiscountedPrice(formation.getPrice());
        }
        return formationRepository.save(formation);
    }

    @Override
    public Page<Formation> getFormations(Pageable pageable) {
        return formationRepository.findAll(pageable);
    }

    @Override
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
        existingFormation.setLabel(formation.getLabel());
        existingFormation.setDuration(formation.getDuration());
        existingFormation.setDiscount(formation.getDiscount());
        existingFormation.setFeatured(formation.getFeatured());
        existingFormation.setHighestRated(formation.getHighestRated());
        // Calculate discounted price
        if (existingFormation.getPrice() != null && existingFormation.getDiscount() != null && existingFormation.getDiscount() > 0) {
            float discounted = existingFormation.getPrice() - (existingFormation.getPrice() * existingFormation.getDiscount() / 100f);
            existingFormation.setDiscountedPrice(discounted);
        } else {
            existingFormation.setDiscountedPrice(existingFormation.getPrice());
        }
        return formationRepository.save(existingFormation);
    }

    @Override
    public void deleteFormation(Integer id) {
        formationRepository.deleteById(id);
    }

    @Override
    public Formation affecterCategoryAFormationByName(Integer id, String categoryName) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formation not found"));

        Category category = categoryRepository.findCategoriesByname(categoryName);
        if (category == null) {
            throw new RuntimeException("Category not found");
        }
        formation.setCategory(category);
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

    @Override
    public Page<Formation> searchFormations(String title, String categoryName, Float minPrice, Float maxPrice, String label, Pageable pageable) {
        return formationRepository.searchFormations(title, categoryName, minPrice, maxPrice, label, pageable);
    }

    @Override
    public Formation updateDiscount(Integer id, Integer discount) {
        Formation formation = formationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Formation not found"));
        formation.setDiscount(discount);
        // recalculate discounted price
        if (formation.getPrice() != null && formation.getDiscount() != null && formation.getDiscount() > 0) {
            float discounted = formation.getPrice() - (formation.getPrice() * formation.getDiscount() / 100f);
            formation.setDiscountedPrice(discounted);
        } else {
            // If discount is 0 or null, set discountedPrice to price
            formation.setDiscountedPrice(formation.getPrice());
        }
        return formationRepository.save(formation);
    }

    @Override
    public Formation findByTitle(String title) {
        return formationRepository.findByTitle(title);
    }

    @Override
    public List<Formation> retrieveAllFormations() {
        return formationRepository.findAll();
    }
}