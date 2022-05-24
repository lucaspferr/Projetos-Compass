package com.MS.catalog;

import com.MS.catalog.model.Category;
import com.MS.catalog.model.DTO.CategoryDTO;
import com.MS.catalog.model.DTO.ProductDTO;
import com.MS.catalog.model.Product;
import com.MS.catalog.repository.CategoryRepository;
import com.MS.catalog.repository.ProductRepository;
import com.MS.catalog.repository.VariationRepository;
import com.MS.catalog.service.CategoryService;
import com.MS.catalog.service.ProductService;
import com.MS.catalog.service.SequenceGeneratorService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Profile("dev")
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private VariationRepository variationRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ModelMapper mapper;
    @Mock
    private SequenceGeneratorService generatorService;

    private Category category;
    private Category category2;
    private Product product;
    private Product product2;
    private ProductDTO productDTO;
    private ProductDTO dtoReturn;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        createProduct();
        createCategory();
    }

    @Test
    void normalToDTO(){
        when(variationRepository.findByVariant_id(1l)).thenReturn(null);
        ModelMapper modelMapper = new ModelMapper();
        dtoReturn = modelMapper.map(productDTO, ProductDTO.class);
        when(mapper.map(any(),eq(ProductDTO.class))).thenReturn(dtoReturn);
        dtoReturn = productService.normalToDTO(product);
        assertEquals(PRODUCT_ID, dtoReturn.getProduct_id());
        assertEquals(P_NAME, dtoReturn.getName());
        assertEquals(DESCRIPTION, dtoReturn.getDescription());
        assertEquals(P_ACTIVE, dtoReturn.getActive());
        assertEquals(CATEGORY_IDS, dtoReturn.getCategory_ids());
    }

    @Test
    void activeCategoryCheckerOK(){
        when(categoryRepository.findByCategory_id(1l)).thenReturn(category);
        when(categoryRepository.findByCategory_id(2l)).thenReturn(category2);
        assertDoesNotThrow(() -> {productService.activeCategoryChecker(product.getCategory_ids());});
    }

    @Test
    void activeCategoryCheckerFail(){
        category2.setActive(false);
        when(categoryRepository.findByCategory_id(1l)).thenReturn(category);
        when(categoryRepository.findByCategory_id(2l)).thenReturn(category2);
        assertThrows(IllegalStateException.class,() -> {productService.activeCategoryChecker(product.getCategory_ids());});
    }

    @Test
    void listDTO(){
        when(productService.normalToDTO(product)).thenReturn(productDTO);
        assertEquals(List.of(productDTO), productService.listDTO(List.of(product)));
    }

    @Test
    void categoryCheckerOK(){
        when(categoryRepository.findByCategory_id(1l)).thenReturn(category);
        when(categoryRepository.findByCategory_id(2l)).thenReturn(category2);
        assertDoesNotThrow(() -> {productService.categoryChecker(List.of(1l,2l));});
    }

    @Test
    void categoryCheckerFail(){
        category2 = null;
        when(categoryRepository.findByCategory_id(1l)).thenReturn(category);
        when(categoryRepository.findByCategory_id(2l)).thenReturn(category2);
        assertThrows(IllegalStateException.class,() -> {productService.categoryChecker(List.of(1l,2l));});
    }

    @Test
    void getProductByIdOK(){
        when(productRepository.findByProduct_id(1l)).thenReturn(product);
        Product productReturn = productService.getProductById(1l);
        assertEquals(PRODUCT_ID, productReturn.getProduct_id());
        assertEquals(P_NAME, productReturn.getName());
        assertEquals(DESCRIPTION, productReturn.getDescription());
        assertEquals(P_ACTIVE, productReturn.getActive());
        assertEquals(CATEGORY_IDS, productReturn.getCategory_ids());
    }

    @Test
    void getProductByIdFail(){
        when(productRepository.findByProduct_id(1l)).thenReturn(null);
        assertThrows(IllegalStateException.class,() -> {productService.getProductById(1l);});
    }



    static final Long PRODUCT_ID = 1l;
    static final Long PRODUCT_ID2 = 2l;
    static final String P_NAME = "Product Name";
    static final String P_NAME2 = "Product 2";
    static final String DESCRIPTION = "Lorem Ipsum";
    static final Boolean P_ACTIVE = true;
    static final List<Long> CATEGORY_IDS = List.of(1l,2l);

    static final Long CATEGORY_ID = 1l;
    static final Long CATEGORY_ID2 = 2l;
    static final String C_NAME = "Category Name";
    static final Boolean C_ACTIVE = true;
    static final Boolean C_ACTIVE_FALSE = false;

    void createCategory(){
        category = new Category(CATEGORY_ID,C_NAME,C_ACTIVE, List.of(product));
        category2 = new Category(CATEGORY_ID2,C_NAME,C_ACTIVE, List.of(product));
    }
    void createProduct(){
        product = new Product(PRODUCT_ID,P_NAME,DESCRIPTION,P_ACTIVE,CATEGORY_IDS, null);
        productDTO = new ProductDTO(PRODUCT_ID,P_NAME,DESCRIPTION,P_ACTIVE,CATEGORY_IDS);
        product2 = new Product(PRODUCT_ID2,P_NAME2,DESCRIPTION,P_ACTIVE,CATEGORY_IDS, null);
    }

}
