package lk.ijse.backend.controller;

import lk.ijse.backend.dto.CategoryDTO;
import lk.ijse.backend.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("getPatients")
    public List<CategoryDTO> getCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/addPatient")
    public CategoryDTO createCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.saveCategory(categoryDTO);
    }

    @PutMapping("/updatePatient")
    public CategoryDTO updateCategory(@RequestBody CategoryDTO categoryDTO){
        return categoryService.updateCategory(categoryDTO);
    }

    @DeleteMapping("/deletePatient/{id}")
    public String deleteCategory(@PathVariable Long id){
        return categoryService.deleteCategory(id);
    }

    @GetMapping("/getPatient/{id}")
    public CategoryDTO getSingleCategory(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }
}