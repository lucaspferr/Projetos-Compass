package com.MS.catalog;

import com.MS.catalog.controller.CategoryController;
import com.MS.catalog.controller.ProductController;
import com.MS.catalog.controller.VariationController;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class VariationControllerTest {

    @InjectMocks
    private VariationController variationController;
    @InjectMocks
    private CategoryController categoryController;
    @InjectMocks
    private ProductController productController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MongoOperations mongoOperations;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    void end(){
        mongoOperations.dropCollection("database_sequences");
        mongoOperations.dropCollection("category");
        mongoOperations.dropCollection("product");
        mongoOperations.dropCollection("variation");
    }

    /*
    1. Try to create a new Variation without Product/Category (Exception)
    2. Create a Category
    3. Create a Product
    4. Create a Variant
    5.
     */

    @Test
    @Order(1)
    void createVariantFail() throws Exception{
        URI uri = new URI("/v1/variations");
        String variant_fail = "{"+VARIANT1_OK+"}";
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content(variant_fail).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(VariationController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((E_PROD_ID+1+E_NOT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    void createCategory() throws Exception{
        URI uri = new URI("/v1/categories");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+CAT1_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((CAT1_ID+CAT1_OK+CAT_PRODUCT+"]}"), result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+CAT2_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((CAT2_ID+CAT2_OK+CAT_PRODUCT+"]}"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    void createProduct() throws Exception{
        URI uri = new URI("/v1/products");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+PROD1_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((PROD1_ID+PROD1_OK+PROD_VARIATION+"]}"), result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+PROD2_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((PROD2_ID+PROD2_OK+PROD_VARIATION+"]}"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    void createVariant() throws Exception{
        URI uri = new URI("/v1/variations");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+VARIANT1_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((VARIANT1_ID+VARIANT1_OK+"}"), result.getResponse().getContentAsString());

        result = mockMvc.perform(MockMvcRequestBuilders.post(uri).content("{"+VARIANT2_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((VARIANT2_ID+VARIANT2_OK+"}"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    void updateVariantFail() throws Exception{
        URI uri = new URI("/v1/variations/5");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri).content("{"+VARIANT1_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(VariationController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((E_VARI_ID+5+E_NOT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    void updateVariantOK() throws Exception{
        URI uri = new URI("/v1/variations/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put(uri).content("{"+NEW_VARIANT_OK+"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201)).andReturn();
        assertEquals((VARIANT1_ID+NEW_VARIANT_OK+"}"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    void deleteVariantFail() throws Exception{
        URI uri = new URI("/v1/variations/5");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(VariationController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((E_VARI_ID+5+E_NOT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(8)
    void getVariationByIdFail() throws Exception{
        URI uri = new URI("/v1/variations/feign/5");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andExpect(handler().handlerType(VariationController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((E_VARI_ID+5+E_NOT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(9)
    void getVariationByIdOK() throws Exception{
        URI uri = new URI("/v1/variations/feign/2");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();

        assertEquals((VARIANT2_ID+VARIANT2_OK+"}"), result.getResponse().getContentAsString());
    }

    @Test
    @Order(10)
    void getProductWithVariations() throws Exception{
        URI uri = new URI("/v1/products/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri))
                .andExpect(MockMvcResultMatchers.status().is(200)).andReturn();
        System.out.println("AQUI ->  "+result.getResponse().getContentAsString());
    }

    @Test
    @Order(11)
    void deleteVariantOK() throws Exception{
        URI uri = new URI("/v1/variations/1");
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete(uri))
                .andExpect(MockMvcResultMatchers.status().is(204)).andReturn();

        assertEquals("", result.getResponse().getContentAsString());
    }


    //Category
    static final String CAT1_OK = "\"name\":\"Category One\",\"active\":true";
    static final String CAT2_OK = "\"name\":\"Category Two\",\"active\":true";
    static final String CAT_PRODUCT = ",\"productList\":[";
    static final String CAT1_ID = "{\"category_id\":1,";
    static final String CAT2_ID = "{\"category_id\":2,";
    //Product
    static final String PROD1_OK = "\"name\":\"Product One\",\"description\":\"Lorem Ipsum\",\"active\":true,\"category_ids\":[1]";
    static final String PROD2_OK = "\"name\":\"Product Two\",\"description\":\"Lorem Ipsum\",\"active\":true,\"category_ids\":[1,2]";
    static final String PROD_VARIATION = ",\"variationList\":[";
    static final String PROD1_ID = "{\"product_id\":1,";
    static final String PROD2_ID = "{\"product_id\":2,";
    //Variant
    static final String VARIANT1_OK = "\"color\":\"Red\",\"size\":\"G\",\"price\":249.99,\"quantity\":10,\"product_id\":1";
    static final String VARIANT2_OK = "\"color\":\"Blue\",\"size\":\"M\",\"price\":149.99,\"quantity\":20,\"product_id\":2";
    static final String NEW_VARIANT_OK = "\"color\":\"Purple\",\"size\":\"P\",\"price\":79.99,\"quantity\":30,\"product_id\":2";
    static final String VARIANT1_ID = "{\"variant_id\":1,";
    static final String VARIANT2_ID = "{\"variant_id\":2,";
    //Errors
    static final String E_PROD_ID = "Product with the ID ";
    static final String E_NOT_EXIST = " doesn't exist.";
    static final String E_VARI_ID = "Variant with the ID ";
}
