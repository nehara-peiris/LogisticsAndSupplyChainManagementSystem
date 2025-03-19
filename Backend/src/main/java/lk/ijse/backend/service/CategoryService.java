package lk.ijse.backend.service;

import lk.ijse.backend.dto.CategoryDTO;

import java.util.List;

public interface CategoryService {
    List<CategoryDTO> getAllCategories();

    CategoryDTO saveCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(CategoryDTO categoryDTO);

    String deleteCategory(Long categoryDTO);

    CategoryDTO getCategoryById(Long categoryDTO);
}
