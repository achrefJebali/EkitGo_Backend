package tn.esprit.examenspring.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.examenspring.Repository.CategoryRepository;
import tn.esprit.examenspring.entities.Category;
import tn.esprit.examenspring.entities.Category;

import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService{

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public List<Category> getCategorys() {
        return categoryRepository.findAll();
    }

    @Override
    public Category modifyCategory(Category category) {
        categoryRepository.save(category);
        return category;
    }

    @Override
    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }
    @Override
    public Category findByName(String name) {
        return categoryRepository.findCategoriesByname(name);
    }

    @Override
    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

}
