package tn.esprit.examenspring.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.examenspring.entities.Category;
import tn.esprit.examenspring.services.ICategoryService;

import java.util.List;

@RestController
@RequestMapping("/Category")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/retrieve-all-category")
    public List<Category> getCategory() {
        return categoryService.getCategorys();
    }

    @PostMapping("/add-category")
    public Category addCategory(@RequestBody Category c) {
        return categoryService.addCategory(c);
    }
    @DeleteMapping("/remove-category/{category-id}")
    public void removeCategory(@PathVariable("category-id") Integer fid) {
        categoryService.deleteCategory(fid);
    }
    @PutMapping("/modify-category")
    public Category modifyCategory(@RequestBody Category c) {
        return categoryService.modifyCategory(c);
    }

    @GetMapping("/retrieve-by-name/{name}")
    public Category getCategoryByname(@PathVariable String name){ return  categoryService.getcategorybyname(name);}


}
