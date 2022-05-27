package com.MS.checkout;

import com.MS.checkout.client.CatalogFeign;
import com.MS.checkout.client.UserFeign;
import com.MS.checkout.controller.CheckoutController;
import com.MS.checkout.model.Purchase;
import com.MS.checkout.model.dto.*;
import com.MS.checkout.rabbit.CartMessageSender;
import com.MS.checkout.rabbit.HistoryMessageSender;
import com.MS.checkout.service.PurchaseService;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.net.URI;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
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
    @Mock
    private PurchaseService purchaseService;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CatalogFeign catalogFeign;
    @MockBean
    private UserFeign userFeign;
    @Mock
    private CartMessageSender cartMessageSender;
    @Mock
    private HistoryMessageSender historyMessageSender;

    private VariationDTO variationDTO;
    private ProductDTO productDTO;
    private CustomerDTO customerDTO;

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

    @Test
    @Order(9)
    void createPurchaseOK() throws Exception{

        variationDTO = new VariationDTO(15l,"Red","M",50.9,5,15l);
        productDTO = new ProductDTO(15l,"T-shirt","Lorem Ipsum",true, List.of(1l));
        customerDTO = new CustomerDTO(15l,true);
        when(catalogFeign.getVariant(15l)).thenReturn(variationDTO);
        when(catalogFeign.getProduct(15l)).thenReturn(productDTO);
        when(userFeign.getCustomer(15l)).thenReturn(customerDTO);
        doNothing().when(purchaseService).sendMessageToCatalog(any());
        doNothing().when(purchaseService).sendMessageToHistory(any());
        doNothing().when(purchaseService).isCustomerActive(15l);

        URI uri = new URI("/v1/purchases");
        MvcResult result = mockMvc.perform(post(uri).content(PURCHASE_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201)).andReturn();

        assertEquals(PURCHASE_RETURN,result.getResponse().getContentAsString());
    }

    @Test
    @Order(10)
    void createPurchaseFailQuantity() throws Exception{

        variationDTO = new VariationDTO(15l,"Red","M",50.9,2,15l);
        productDTO = new ProductDTO(15l,"T-shirt","Lorem Ipsum",true, List.of(1l));
        customerDTO = new CustomerDTO(15l,true);
        when(catalogFeign.getVariant(15l)).thenReturn(variationDTO);
        when(catalogFeign.getProduct(15l)).thenReturn(productDTO);
        when(userFeign.getCustomer(15l)).thenReturn(customerDTO);
        doNothing().when(purchaseService).sendMessageToCatalog(any());
        doNothing().when(purchaseService).sendMessageToHistory(any());
        doNothing().when(purchaseService).isCustomerActive(15l);

        URI uri = new URI("/v1/purchases");
        MvcResult result = mockMvc.perform(post(uri).content(PURCHASE_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals(QUANTITY_ERROR, result.getResponse().getContentAsString());

    }

    @Test
    @Order(11)
    void createPurchaseFailProdActive() throws Exception{

        variationDTO = new VariationDTO(15l,"Red","M",50.9,5,15l);
        productDTO = new ProductDTO(15l,"T-shirt","Lorem Ipsum",false, List.of(1l));
        customerDTO = new CustomerDTO(15l,true);
        when(catalogFeign.getVariant(15l)).thenReturn(variationDTO);
        when(catalogFeign.getProduct(15l)).thenReturn(productDTO);
        when(userFeign.getCustomer(15l)).thenReturn(customerDTO);
        doNothing().when(purchaseService).sendMessageToCatalog(any());
        doNothing().when(purchaseService).sendMessageToHistory(any());
        doNothing().when(purchaseService).isCustomerActive(15l);

        URI uri = new URI("/v1/purchases");
        MvcResult result = mockMvc.perform(post(uri).content(PURCHASE_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals(PROD_INACTIVE, result.getResponse().getContentAsString());

    }

    @Test
    @Order(12)
    void createPurchaseFailCustomerActive() throws Exception{

        variationDTO = new VariationDTO(15l,"Red","M",50.9,5,15l);
        productDTO = new ProductDTO(15l,"T-shirt","Lorem Ipsum",true, List.of(1l));
        customerDTO = new CustomerDTO(15l,false);
        when(catalogFeign.getVariant(15l)).thenReturn(variationDTO);
        when(catalogFeign.getProduct(15l)).thenReturn(productDTO);
        when(userFeign.getCustomer(15l)).thenReturn(customerDTO);
        doNothing().when(purchaseService).sendMessageToCatalog(any());
        doNothing().when(purchaseService).sendMessageToHistory(any());
        doNothing().when(purchaseService).isCustomerActive(15l);

        URI uri = new URI("/v1/purchases");
        MvcResult result = mockMvc.perform(post(uri).content(PURCHASE_OK).contentType(MediaType.APPLICATION_JSON))
                .andExpect(handler().handlerType(CheckoutController.class))
                .andExpect(status().is(400)).andReturn();
        assertTrue(result.getResolvedException() instanceof IllegalStateException);
        assertEquals(USER_INACTIVE, result.getResponse().getContentAsString());

    }

    @Test
    @Order(13)
    void priceCalculator(){
        PaymentDTO paymentDTO = new PaymentDTO("Test",10.00,true);
        ProductHistory product1 = new ProductHistory("Test","Lorem Ipsum","Red","M",99.90,5);
        ProductHistory product2 = new ProductHistory("Test","Lorem Ipsum","Red","M",49.90,2);
        PurchaseHistory purchaseHistory = new PurchaseHistory(1l,paymentDTO,List.of(product1,product2),0.00,"27/05/2022");
        PurchaseService service = new PurchaseService();
        Double realPrice = ((product1.getPrice()*product1.getQuantity())+(product2.getPrice()*product2.getQuantity()))*0.90;

        assertEquals(realPrice, service.priceCalculator(purchaseHistory));
    }


    //Payment
    static final String PAY1_OK = "\"type\":\"Test\",\"discount\":11.11,\"status\":true}";
    static final String PAY2_OK = "\"type\":\"New test\",\"discount\":22.22,\"status\":true}";
    static final String PAY_UPDATED = "\"type\":\"Updated Test\",\"discount\":33.33,\"status\":true}";
    static final String PAY1_ID = "{\"payment_id\":1,";
    static final String PAY2_ID = "{\"payment_id\":2,";
    static final String PAY_FAIL1 = "{}";
    static final String PAY_FAIL2 = "{\"type\":\"Test\",\"discount\":10.00}";
    //Cart
    static final String PURCHASE_OK = "{\"user_id\":15,\"payment_id\":2,\"cart\":[{\"variant_id\":15,\"quantity\":5}]}";
    static final String PURCHASE_RETURN = "{\"purchase_id\":1,\"user_id\":15,\"payment_id\":2,\"cart\":[{\"cart_id\":1,\"variant_id\":15,\"quantity\":5}]}";
    //Exception
    static final String FIELDS_MISS = "The field(s) bellow must be filled:";
    static final String TYPE = "\n* Type";
    static final String DISCOUNT = "\n* Discount";
    static final String STATUS = "\n* Status";
    static final String METHOD_EXIST = "Payment method with the ID ";
    static final String DOESNT_EXIST = " doesn't exist.";
    static final String QUANTITY_ERROR = "Product with insufficient quantity.";
    static final String PROD_INACTIVE = "Product not active.";
    static final String USER_INACTIVE = "User with the ID 15 isn't active.";

}
