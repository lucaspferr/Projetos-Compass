package com.MS.shopstyle;

import com.MS.shopstyle.controller.CustomerController;
import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.Sex;
import com.MS.shopstyle.repository.CustomerRepository;
import com.MS.shopstyle.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@AutoConfigureMockMvc
@Profile("dev")
public class CustomerControllerTest {

    @InjectMocks
    private CustomerController controller;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private CustomerService service;

    private Customer customer;
    private CustomerDTO customerDTO;
    private Optional<Customer> optionalCustomer;
    private ResponseEntity response;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loginFailed() throws Exception {
        URI uri = new URI("/v1/login");
        String json = "{\"email\":\"fail@teste.com\",\"password\":\"testepass\"}";

        mockMvc.perform(MockMvcRequestBuilders
                .post(uri)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void createCustomerFailed() throws Exception{
        URI uri = new URI("/v1/users");
        String json = "{\"email\":\"fail@teste.com\",\"password\":\"testepass\"}";


        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(400));
    }

    @Test
    void createCustomerOk() throws Exception{
        URI uri = new URI("/v1/users");
        String json = "{\"firstName\":\"Teste\"," +
                "\"lastName\":\"dos Testes\"," +
                "\"sex\":\"Masculino\"," +
                "\"cpf\":\"111.111.111-11\","+
                "\"birthdate\":\"19/04/1997\","+
                "\"email\":\"teste@teste.com\","+
                "\"password\":\"testepass\","+
                "\"active\":true}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    void updateCustomerOk() throws Exception{
        URI uri = new URI("/v1/users/1");
        String json = "{\"firstName\":\"Teste\"," +
                "\"lastName\":\"dos Testes\"," +
                "\"sex\":\"Masculino\"," +
                "\"cpf\":\"111.111.111-11\","+
                "\"birthdate\":\"19/04/1997\","+
                "\"email\":\"teste3@teste.com\","+
                "\"password\":\"testepass\","+
                "\"active\":true}";

        mockMvc.perform(MockMvcRequestBuilders
                        .put(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(201));
    }

    @Test
    void loginOk() throws Exception {
        URI uri = new URI("/v1/login");
        String json = "{\"email\":\"teste@teste.com\",\"password\":\"testepass\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is(200));
    }

    @Test
    void getOne() throws Exception{
        URI uri = new URI("/v1/users/1");

        mockMvc.perform(MockMvcRequestBuilders
                .get(uri)).andExpect(MockMvcResultMatchers.status().is(200));

    }


}
