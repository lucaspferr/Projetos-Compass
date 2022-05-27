package com.MS.checkout;

import com.MS.checkout.controller.CheckoutController;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.URI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckoutControllerTest {

    @InjectMocks
    private CheckoutController checkoutController;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Order(1)
    void createPaymentMethodFail() throws Exception{
        URI uri = new URI("/v1/payments");
        MvcResult result = mockMvc.perform(post(uri).content(PAY_FAIL1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(CheckoutController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
        assertEquals((FIELDS_MISS+TYPE+DISCOUNT+STATUS), result.getResponse().getContentAsString());

        result = mockMvc.perform(post(uri).content(PAY_FAIL2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(CheckoutController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
        assertEquals((FIELDS_MISS+STATUS), result.getResponse().getContentAsString());
    }

    @Test
    @Order(2)
    void getPaymentoMethodByIdFail() throws Exception{
        URI uri = new URI("/v1/payments/1");
        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((METHOD_EXIST+1+DOESNT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(3)
    void createPaymentMethodOK() throws Exception{
        URI uri = new URI("/v1/payments");
        MvcResult result = mockMvc.perform(post(uri).content("{"+PAY1_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();
        assertEquals((PAY1_ID+PAY1_OK), result.getResponse().getContentAsString());

        result = mockMvc.perform(post(uri).content("{"+PAY2_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();
        assertEquals((PAY2_ID+PAY2_OK), result.getResponse().getContentAsString());
    }

    @Test
    @Order(4)
    void getPaymentMethodByIdOK() throws Exception{
        URI uri = new URI("/v1/payments/1");
        MvcResult result = mockMvc.perform(get(uri))
                .andExpect(status().is(200)).andReturn();
        assertEquals((PAY1_ID+PAY1_OK), result.getResponse().getContentAsString());
    }

    @Test
    @Order(5)
    void getAllMethods() throws Exception{
        URI uri = new URI("/v1/payments");
        MvcResult result = mockMvc.perform(get(uri)).andExpect(status().is(200)).andReturn();
        assertEquals(("["+PAY1_ID+PAY1_OK+","+PAY2_ID+PAY2_OK+"]"),result.getResponse().getContentAsString());
    }

    @Test
    @Order(6)
    void putPaymentFail() throws Exception{
        URI uri = new URI("/v1/payments/1");
        MvcResult result = mockMvc.perform(put(uri).content(PAY_FAIL2).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(CheckoutController.class)).andReturn();
        assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException);
        assertEquals((FIELDS_MISS+STATUS), result.getResponse().getContentAsString());

        uri = new URI("/v1/payments/10");
        result = mockMvc.perform(put(uri).content("{"+PAY_UPDATED).contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((METHOD_EXIST+10+DOESNT_EXIST), result.getResponse().getContentAsString());
    }

    @Test
    @Order(7)
    void putPaymentOK() throws Exception{
        URI uri = new URI("/v1/payments/2");
        MvcResult result = mockMvc.perform(put(uri).content("{"+PAY_UPDATED).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();
        assertEquals((PAY2_ID+PAY_UPDATED), result.getResponse().getContentAsString());

        uri = new URI("/v1/payments");
        result = mockMvc.perform(get(uri)).andExpect(status().is(200)).andReturn();
        assertEquals(("["+PAY1_ID+PAY1_OK+","+PAY2_ID+PAY_UPDATED+"]"),result.getResponse().getContentAsString());
    }

    @Test
    @Order(8)
    void deletePaymentOK() throws Exception{
        URI uri = new URI("/v1/payments/1");
        MvcResult result = mockMvc.perform(delete(uri))
                .andExpect(status().is(204)).andReturn();
        assertEquals("", result.getResponse().getContentAsString());

        uri = new URI("/v1/payments/1");
        result = mockMvc.perform(get(uri))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals((METHOD_EXIST+1+DOESNT_EXIST), result.getResponse().getContentAsString());
    }


    //Payment
    static final String PAY1_OK = "\"type\":\"Test\",\"discount\":11.11,\"status\":true}";
    static final String PAY2_OK = "\"type\":\"New test\",\"discount\":22.22,\"status\":true}";
    static final String PAY_UPDATED = "\"type\":\"Updated Test\",\"discount\":33.33,\"status\":true}";
    static final String PAY1_ID = "{\"payment_id\":1,";
    static final String PAY2_ID = "{\"payment_id\":2,";
    static final String PAY_FAIL1 = "{}";
    static final String PAY_FAIL2 = "{\"type\":\"Test\",\"discount\":10.00}";
    //Exception
    static final String FIELDS_MISS = "The field(s) bellow must be filled:";
    static final String TYPE = "\n* Type";
    static final String DISCOUNT = "\n* Discount";
    static final String STATUS = "\n* Status";
    static final String METHOD_EXIST = "Payment method with the ID ";
    static final String DOESNT_EXIST = " doesn't exist.";

}
