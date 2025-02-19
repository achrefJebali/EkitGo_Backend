package tn.esprit.examenspring.services;

import tn.esprit.examenspring.entities.Category;

import java.util.List;

public interface ICategoryService {

    Category addCategory(Category category);
    List<Category> getCategorys();
    Category modifyCategory(Category category);
    void deleteCategory(Integer id);
    Category getcategorybyname(String name);
}
