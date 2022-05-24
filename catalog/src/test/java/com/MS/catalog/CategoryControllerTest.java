package com.MS.catalog;

import com.MS.catalog.controller.CategoryController;
import com.MS.catalog.service.CategoryService;
import com.mongodb.client.MongoClients;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CategoryControllerTest {

    @InjectMocks
    private CategoryController categoryController;
    @Autowired
    private MockMvc mockMvc;
    @Mock
    private CategoryService categoryService;

    @Autowired
    private MongoOperations mongoOperations;


    @BeforeEach
    void init(){MockitoAnnotations.openMocks(this);}



    @AfterAll
    void end(){
        System.out.println(mongoOperations.getCollectionNames());
        mongoOperations.dropCollection("database_sequences");
        mongoOperations.dropCollection("category");
    }

    @Test
    @Order(1)
    void createCategoryOk() throws Exception{
        URI uri = new URI("/v1/categories");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                .post(uri).content(CAT_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    void createCategoryFail() throws Exception{
        URI uri = new URI("/v1/categories");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri).content(CAT_FAIL_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(CategoryController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
        assertEquals((CAT_MISSING_JSON+CAT_VALID), result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    void getAllCategoryOk() throws Exception{
        URI uri = new URI("/v1/categories");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(("["+CAT_MONGO_ALL+"]"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    void updateCategoryOK() throws Exception{
        URI uri = new URI("/v1/categories/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .put(uri).content(CAT_UP_OK_JSON).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals(CAT_MONGO_UP, result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    void getOneCategory() throws Exception{
        URI uri = new URI("/v1/categories/1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        assertEquals(CAT_MONGO_UP, result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    void deleteCategory() throws Exception{
        URI uri = new URI("/v1/categories/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().is(204)).andReturn();
        System.out.println(result.getResponse().getContentAsString());
        assertEquals("",result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    void findByIdFail() throws Exception{
        URI uri = new URI("/v1/categories/1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(CategoryController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((CAT_FIND_FAIL), result.getResponse().getContentAsString());
    }

    static final String CAT_OK_JSON = "{\"name\":\"Teste name\",\"active\":\"true\"}";
    static final String CAT_UP_OK_JSON = "{\"name\":\"New name\",\"active\":\"false\"}";
    static final String CAT_FAIL_JSON = "{\"name\":\"Teste name\"}";
    static final String CAT_MISSING_JSON = "The field(s) bellow must be filled:";
    static final String CAT_NAME = "\n* Name";
    static final String CAT_VALID = "\n* Valid";
    static final String CAT_MONGO_ALL = "{\"category_id\":1,\"name\":\"Teste name\",\"active\":true}";
    static final String CAT_MONGO_UP = "{\"category_id\":1,\"name\":\"New name\",\"active\":false,\"productList\":[]}";
    static final String CAT_MONGO_POST = "{\"category_id\":1,\"name\":\"New name\",\"active\":false,\"productList\":[]}";
    static final String CAT_FIND_FAIL = "Category with the ID 1 doesn't exist.";


}
