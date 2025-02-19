package tn.esprit.examenspring.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.examenspring.entities.Category;
@Repository

public interface CategoryRepository extends JpaRepository<Category,Integer> {
    Category findCategoriesByname (String name);
}
