package com.MS.catalog;

import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.service.CategoryService;
import com.MS.catalog.service.ProductService;
import com.MS.catalog.service.SequenceGeneratorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Profile("dev")
public class CategoryServiceTest{

    @InjectMocks
    private CategoryService categoryService;
    @Mock
    private CategoryRepository repository;
    @Mock
    private ProductService productService;
    @Mock
    private SequenceGeneratorService generatorService;
    @Mock
    private ModelMapper mapper;

    private Category category;
    private CategoryDTO categoryDTO;
    private CategoryDTO dtoReturn = new CategoryDTO();
    private Category catReturn = new Category();
    private List<Category> categoryList;
    private List<CategoryDTO> categoryDTOList;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        createCategory();
    }

    @Test
    void getAllCategoriesTest(){
        when(repository.findAll()).thenReturn(categoryList);
        when(mapper.map(any(), eq(CategoryDTO.class))).thenReturn(categoryDTO);
        assertEquals(categoryDTOList, categoryService.getAllCategories());
    }

    @Test
    void getOneCategoryTest(){
        Long id = category.getCategory_id();
        when(repository.findByCategory_id(id)).thenReturn(category);
        assertEquals(category, categoryService.getCategoryById(id));
    }

    @Test
    void createCategoryTest(){
        when(mapper.map(any(), eq(Category.class))).thenReturn(category);
        when(generatorService.generateSequence(any())).thenReturn(1l);
        when(repository.save(any())).thenReturn(category);
        catReturn = categoryService.createCategory(categoryDTO);
        System.out.println(catReturn);
        assertEquals(CATEGORY_ID,catReturn.getCategory_id());
        assertEquals(NAME, catReturn.getName());
        assertEquals(ACTIVE, catReturn.getActive());
    }

    @Test
    void updateCategoryTest(){
        when(mapper.map(any(), eq(Category.class))).thenReturn(category);
        when(repository.save(any())).thenReturn(category);
        when(repository.findByCategory_id(1l)).thenReturn(category);

        assertEquals(category, categoryService.updateCategory(1l,categoryDTO));
    }




    void createCategory(){
        category = new Category(CATEGORY_ID, NAME, ACTIVE, null);
        categoryDTO = new CategoryDTO(CATEGORY_ID, NAME, ACTIVE);
        categoryList = new ArrayList<>();
        categoryDTOList = new ArrayList<>();
        categoryList.add(category);
        categoryDTOList.add(categoryDTO);
    }

    static final Long CATEGORY_ID = 1l;
    static final String NAME = "Category Name";
    static final Boolean ACTIVE = true;

}
