package com.MS.catalog;

import com.MS.catalog.controller.CategoryController;
import com.MS.catalog.controller.ProductController;
import com.MS.catalog.service.CategoryService;
import com.MS.catalog.service.ProductService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerTest {

    @InjectMocks
    private ProductController productController;
    @InjectMocks
    private CategoryController categoryController;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private ProductService productService;
    @Mock
    private CategoryService categoryService;
    @Autowired
    private MongoOperations mongoOperations;

    @BeforeEach
    void init(){MockitoAnnotations.openMocks(this);}

    @AfterAll
    void end(){
        mongoOperations.dropCollection("database_sequences");
        mongoOperations.dropCollection("category");
        mongoOperations.dropCollection("product");
    }

    @Test
    @Order(1)
    void getOneProductFail() throws Exception{
        URI uri = new URI("/v1/products/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(ProductController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals(PROD_FIND_FAIL, result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    void createCategoryOk() throws Exception{
        URI uri = new URI("/v1/categories");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri).content(CAT_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    void createProductOK() throws Exception{
        URI uri = new URI("/v1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(PROD_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals(PROD_OK_ID_JSON, result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(PROD_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals(PROD_OK_ID2_JSON, result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    void getOneProductOK() throws Exception{
        URI uri = new URI("/v1/products/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(PROD_OK_ID_JSON, result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    void getAllProducts() throws Exception{
        URI uri = new URI("/v1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(("["+PROD_OK_ID_JSON+","+PROD_OK_ID2_JSON+"]"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    void updateProductOk() throws Exception{
        URI uri = new URI("/v1/products/2");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri).content(PROD_OK2_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals(PROD_OK_ID_UP_JSON, result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    void deleteProduct() throws Exception{
        URI uri = new URI("/v1/products/2");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().is(204)).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    @Order(8)
    void updateCategoryToInactive() throws Exception{
        URI uri = new URI("/v1/categories/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(uri).content(CAT_INACTIVE_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals(CAT_INACTIVE_ID_JSON,result.getResponse().getContentAsString());
    }

    @Test
    @Order(9)
    void errorCreatingProductBecauseCategoryIsInactive() throws Exception{
        URI uri = new URI("/v1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(PROD_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(ProductController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals(CAT_INACTIVE_FAIL, result.getResponse().getContentAsString());
    }

    @Test
    @Order(10)
    void deleteCategory() throws Exception{
        URI uri = new URI("/v1/categories/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().is(204)).andReturn();
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    @Order(11)
    void productSetToInactiveBecauseCategoryWasDeleted() throws Exception{
        URI uri = new URI("/v1/products/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(PROD_INACTIVE_ID_JSON, result.getResponse().getContentAsString());
    }




    static final String CAT_OK_JSON = "{\"name\":\"Category name\",\"active\":\"true\"}";
    static final String CAT_INACTIVE_JSON = "{\"name\":\"Category name\",\"active\":\"false\"}";
    static final String CAT_INACTIVE_ID_JSON = "{\"category_id\":1,\"name\":\"Category name\",\"active\":false,\"productList\":[]}";
    static final String PROD_OK_JSON = "{\"name\":\"Product name\",\"description\":\"Lorem Ipsum\",\"category_ids\":[1],\"active\":\"true\"}";
    static final String PROD_OK2_JSON = "{\"name\":\"New Product\",\"description\":\"Lorem Ipsum\",\"category_ids\":[1],\"active\":\"true\"}";
    static final String PROD_OK_ID_JSON = "{\"product_id\":1,\"name\":\"Product name\",\"description\":\"Lorem Ipsum\",\"active\":true,\"category_ids\":[1],\"variationList\":[]}";
    static final String PROD_INACTIVE_ID_JSON = "{\"product_id\":1,\"name\":\"Product name\",\"description\":\"Lorem Ipsum\",\"active\":false,\"category_ids\":[],\"variationList\":[]}";
    static final String PROD_OK_ID2_JSON = "{\"product_id\":2,\"name\":\"Product name\",\"description\":\"Lorem Ipsum\",\"active\":true,\"category_ids\":[1],\"variationList\":[]}";
    static final String PROD_OK_ID_UP_JSON = "{\"product_id\":2,\"name\":\"New Product\",\"description\":\"Lorem Ipsum\",\"active\":true,\"category_ids\":[1],\"variationList\":[]}";
    static final String PROD_FIND_FAIL = "Product with the ID 1 doesn't exist.";
    static final String CAT_INACTIVE_FAIL = "Category with the ID 1 is not active.";
}
