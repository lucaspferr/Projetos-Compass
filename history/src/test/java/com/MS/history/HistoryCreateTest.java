package com.MS.history;

import com.MS.history.client.UserFeign;
import com.MS.history.model.*;
import com.MS.history.model.DTO.*;
import com.MS.history.service.HistoryService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoOperations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@Profile("dev")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HistoryCreateTest {

//    @InjectMocks
//    private HistoryService historyService;
    @Autowired
    private HistoryService historyService;
    @Autowired
    private MongoOperations mongoOperations;
    @MockBean
    private UserFeign userFeign;

    private History history;
    private HistoryDTO historyDTO;
    private PaymentMethod paymentMethod;
    private PaymentDTO paymentDTO;
    private Products products;
    private ProductsDTO productsDTO;
    private Purchases purchases;
    private PurchasesDTO purchasesDTO;
    private User user;
    private UserDTO newUserDTO;
    private UserDTO userDTO;
    private CheckoutHistory checkoutHistory;
    private UserForm userForm;
    private UserForm newUserForm;
    private ModelMapper mapperReal = new ModelMapper();

    @BeforeEach
    void init(){
        MockitoAnnotations.openMocks(this);
        createAll();
    }

    @AfterAll
    void end(){
        mongoOperations.dropCollection("database_sequences");
        mongoOperations.dropCollection("history");
        mongoOperations.dropCollection("paymentMethod");
        mongoOperations.dropCollection("products");
        mongoOperations.dropCollection("purchases");
        mongoOperations.dropCollection("user");
    }

    @Test
    @Order(1)
    void createHistory(){
        when(userFeign.getCustomer(1l)).thenReturn(userDTO);
        historyService.postHistory(checkoutHistory);
        assertEquals(historyDTO, historyService.getById(1l));
        System.out.println(historyDTO);
    }

    @Test
    @Order(2)
    void createHistoryUpdatedCustomer(){
        when(userFeign.getCustomer(1l)).thenReturn(newUserDTO);
        historyService.postHistory(checkoutHistory);
        assertEquals(newUserForm, historyService.getById(1l).getUser());
        System.out.println(newUserForm);
    }

    @Test
    @Order(3)
    void getHistoryUpdated(){
        System.out.println(historyService.getById(1l));
    }

    void createAll(){
        user = new User(1l,FNAME,LNAME,SEX,CPF,BIRTH,EMAIL,1l);
        userDTO = new UserDTO(1l,FNAME,LNAME,SEX,CPF,BIRTH,EMAIL);
        newUserDTO = new UserDTO(1l,FNAME2,LNAME2,SEX,CPF,BIRTH,EMAIL2);
        paymentMethod = new PaymentMethod(1l,TYPE,DSCNT,true,1l);
        products = new Products(1l,PNAME,DESCR,COLOR,SIZE,PRICE,5,1l);
        purchases = new Purchases(1l,paymentMethod, List.of(products),TOTAL,DATEBR,1l);
        history = new History(1l,user,List.of(purchases));
        paymentDTO = new PaymentDTO(TYPE, DSCNT,true);
        productsDTO = new ProductsDTO(PNAME,DESCR,COLOR,SIZE,PRICE,5);
        checkoutHistory = new CheckoutHistory(1l,paymentDTO,List.of(productsDTO),TOTAL,DATELOCAL);
        userForm = new UserForm(FNAME,LNAME,SEX,CPF,BIRTH,EMAIL);
        newUserForm = new UserForm(FNAME2,LNAME2,SEX,CPF,BIRTH,EMAIL2);
        purchasesDTO = new PurchasesDTO(paymentDTO,List.of(productsDTO),TOTAL,DATEBR);
        historyDTO = new HistoryDTO(userForm,List.of(purchasesDTO));
    }

    static final String DATELOCAL = "2022-05-27";
    static final String DATEBR = "27/05/2022";
    static final String FNAME = "Firstname";
    static final String FNAME2 = "Updated";
    static final String LNAME = "Lastname";
    static final String LNAME2 = "Customer";
    static final String SEX = "Masculino";
    static final String CPF = "000.000.000-00";
    static final String BIRTH = "19/04/1997";
    static final String EMAIL = "teste@teste.com";
    static final String EMAIL2 = "new@email.com";
    static final String TYPE = "Credit";
    static final Double DSCNT = 15.00;
    static final String PNAME = "Product";
    static final String DESCR = "Lorem Ipsum";
    static final String COLOR = "Red";
    static final String SIZE = "G";
    static final Double PRICE = 120.00;
    static final Double TOTAL = 510.00;
}
