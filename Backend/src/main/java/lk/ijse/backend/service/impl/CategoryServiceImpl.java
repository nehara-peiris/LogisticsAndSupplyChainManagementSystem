package lk.ijse.backend.service.impl;

import lk.ijse.backend.dto.CategoryDTO;
import lk.ijse.backend.entity.Category;
import lk.ijse.backend.repo.CategoryRepository;
import lk.ijse.backend.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public List<CategoryDTO> getAllCategories(){
        List<Category> categoryList = categoryRepository.findAll();
        return modelMapper.map(categoryList, new TypeToken<List<CategoryDTO>>(){}.getType());
    }

    public CategoryDTO saveCategory(CategoryDTO categoryDTO){
        categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        return categoryDTO;
    }

    public CategoryDTO updateCategory(CategoryDTO categoryDTO){
        categoryRepository.save(modelMapper.map(categoryDTO, Category.class));
        return categoryDTO;
    }

    public String deleteCategory(Long categoryDTO){
        categoryRepository.deleteById(categoryDTO);
        return "category deleted";
    }

    public CategoryDTO getCategoryById(Long categoryDTO){
        Optional<Category> category = categoryRepository.findById(categoryDTO);
        return modelMapper.map(category, CategoryDTO.class);
    }

}


//https://github.com/WilliamBuntu/Spring-boot-CRUD/blob/master/src/main/java/com/pokemonreview/api/service/impl/PokemonServiceImpl.java