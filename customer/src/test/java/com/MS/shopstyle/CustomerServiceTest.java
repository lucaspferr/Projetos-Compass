package com.MS.shopstyle;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.Sex;
import com.MS.shopstyle.repository.CustomerRepository;
import com.MS.shopstyle.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @Mock
    private CustomerRepository mockedRepository;

    @InjectMocks
    private CustomerService service;

    @Mock
    private BCryptPasswordEncoder bCrypt;

    @Mock
    private ModelMapper modelMapper;

    private Customer customer;
    private Customer emailCustomer;
    private Customer cReturn;
    private CustomerDTO dtoReturn;
    private CustomerDTO customerDTO;
    private Optional<Customer> optionalCustomer;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
        createCustomer();
    }

    static final Long ID = 1l;
    static final String FIRSTNAME = "Lucas";
    static final String LASTNAME = "Pimentel";
    static final Sex SEX = Sex.MASCULINO;
    static final String CPF = "000.000.000-00";
    static final LocalDate BIRTHDATE = LocalDate.of(1997,04,19);
    static final String EMAIL = "teste@teste.com";
    static final String EMAIL2 = "teste@teste.com.br";
    static final String PASSWORD = "12345678";
    static final Boolean ACTIVE = true;

    @Test
    void saveCustomer(){

        when(mockedRepository.save(any())).thenReturn(customer);
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);
        when(bCrypt.encode(any())).thenReturn("12345678");

        customerDTO.setPassword(bCrypt.encode(customerDTO.getPassword()));

        cReturn = service.createCustomer(customerDTO);

        System.out.println(cReturn);

        assertEquals(Customer.class, cReturn.getClass());
        assertEquals(customer,cReturn);
        assertEquals(ID,cReturn.getId());
        assertEquals(FIRSTNAME,cReturn.getFirstName());
        assertEquals(LASTNAME,cReturn.getLastName());
        assertEquals(SEX,cReturn.getSex());
        assertEquals(CPF, cReturn.getCpf());
        assertEquals(BIRTHDATE,cReturn.getBirthdate());
        assertEquals(PASSWORD,cReturn.getPassword());
        assertEquals(EMAIL2,cReturn.getEmail());
        assertEquals(ACTIVE,cReturn.getActive());
    }

    @Test
    void saveCustomerWithNulls(){
        dtoReturn = new CustomerDTO();
        cReturn = new Customer();
        mockedRepository.save(cReturn);
    }

    @Test
    void localDatetoStringConversor(){
        //Send a LocalDate and return a String onf dd/mm/yyyy format
        assertEquals("19/04/1997",service.dateConversor(LocalDate.of(1997,04,19)));
        //Since the date is fetched on the database and happened previous checks to save the date, there's no necessity to test errors
    }

    @Test
    void stringToLocalDateConverse(){
        //must send a string in the format dd/mm/yyyy and format to LocalDate format yyyy-mm-dd
        assertEquals(LocalDate.of(1997,04,19),service.dateConversor("19/04/1997"));
        //if sent an invalid format, it will throw an Exception that will be handled
        Assertions.assertThrows(IllegalStateException.class, () -> {service.dateConversor("RandomWords");});
        Assertions.assertThrows(IllegalStateException.class, () -> {service.dateConversor("19-04-1997");});
        Assertions.assertThrows(IllegalStateException.class, () -> {service.dateConversor("04/19/1997");});
        Assertions.assertThrows(IllegalStateException.class, () -> {service.dateConversor("19A04A1997");});
    }

    @Test
    void passwordIsEncrypto(){
        //Send a String and expect a different value based on the encrypter
        Assertions.assertNotEquals("12345678",service.passwordEncrypter("12345678"));
    }

    @Test
    void findOneCustomer(){
        when(mockedRepository.findById(any())).thenReturn(optionalCustomer);
        when(modelMapper.map(any(), eq(CustomerDTO.class))).thenReturn(customerDTO);

        dtoReturn = service.findOne(1l);

        assertEquals(customerDTO, service.findOne(1l));
    }

    @Test
    void updateCustomerTest(){
        when(mockedRepository.findById(any())).thenReturn(optionalCustomer);
        when(mockedRepository.save(any())).thenReturn(customer);
        when(mockedRepository.findAll()).thenReturn(List.of(emailCustomer));
        when(modelMapper.map(customerDTO, Customer.class)).thenReturn(customer);

        cReturn = service.updateCustomer(any(), customerDTO);

        assertEquals(customer, cReturn);
    }

    @Test
    void duplicatedEmailCheckerThrows(){
        when(mockedRepository.findAll()).thenReturn(List.of(customer));
        assertThrows(IllegalStateException.class, () -> {
            service.emailDuplicatedChecker("teste@teste.com.br");
        });
    }

    @Test
    void duplicatedEmailCheckerSuccess(){
        when(mockedRepository.findAll()).thenReturn(List.of(emailCustomer));
        assertEquals("teste@teste.com.br",service.emailDuplicatedChecker("teste@teste.com.br"));
    }
    void createCustomer(){
        customer = new Customer(ID,FIRSTNAME,LASTNAME,SEX,CPF,BIRTHDATE,EMAIL2,PASSWORD,ACTIVE);
        emailCustomer = new Customer(ID,FIRSTNAME,LASTNAME,SEX,CPF,BIRTHDATE,EMAIL,PASSWORD,ACTIVE);
        customerDTO = new CustomerDTO(FIRSTNAME,LASTNAME,"Masculino",CPF,"19/04/1997",EMAIL2,PASSWORD,ACTIVE);
        optionalCustomer = optionalCustomer.of(new Customer(ID,FIRSTNAME,LASTNAME,SEX,CPF,BIRTHDATE,EMAIL2,PASSWORD,ACTIVE));
    }

}