package com.MS.catalog.service;

import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.repository.ProductRepository;
import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.model.Product;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductService productService;
    @Autowired
    SequenceGeneratorService sequenceGeneratorService;

    ModelMapper modelMapper = new ModelMapper();

    public List<CategoryDTO> getAllCategories() {
        List<CategoryDTO> categoryDTOList = new ArrayList<>();
        categoryRepository.findAll().forEach(c ->{
            CategoryDTO categoryDTO = modelMapper.map(c, CategoryDTO.class);
            categoryDTOList.add(categoryDTO);
        });
        return categoryDTOList;
    }

    public Category createCategory(CategoryDTO categoryDTO){
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategory_id(sequenceGeneratorService.generateSequence(Category.SEQUENCE_NAME));
        return categoryRepository.save(category);
    }

    public Category getCategoryById(long category_id){

        Category category = categoryRepository.findByCategory_id(category_id);
        //categoryDTO.setProduct_id(productService.getByCategoryId(category_id));
        return category;
    }

    @Transactional
    public Category updateCategory(long category_id, CategoryDTO categoryDTO) {
        try {
            Category idCategory = categoryRepository.findByCategory_id(category_id);
            Category category = modelMapper.map(categoryDTO, Category.class);
            category.setCategory_id(idCategory.getCategory_id());
            return categoryRepository.save(category);
        }catch (Exception e){
            throw new IllegalStateException("Houve um erro com o update de dados.");
        }
    }

    public boolean deleteCategory(long category_id){
        try {
            categoryRepository.deleteByCategory_id(category_id);
            productService.setActiveToFalse(category_id);
            return true;
        }catch (Exception e){return false;}
    }
}
