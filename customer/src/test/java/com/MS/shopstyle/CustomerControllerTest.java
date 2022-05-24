package com.MS.shopstyle;

import com.MS.shopstyle.controller.CustomerController;
import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.net.URI;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().is(400));
    }

    @Test
    void createCustomerFailed() throws Exception{
        URI uri = new URI("/v1/users");
        String json = "{\"email\":\"fail@teste.com\",\"password\":\"testepass\"}";
        String error= "The field(s) bellow must be filled:\n* First name\n* Last name\n* Sex\n* CPF\n* Birthdate\n* Active";


        MvcResult result = mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(handler().handlerType(CustomerController.class))
                .andReturn();
        Assertions.assertEquals(error, result.getResponse().getContentAsString());
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
                .andExpect(status().is(201));
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
                .andExpect(status().is(201));
    }

    @Test
    void loginOk() throws Exception {
        URI uri = new URI("/v1/login");
        String json = "{\"email\":\"teste@teste.com\",\"password\":\"testepass\"}";

        mockMvc.perform(MockMvcRequestBuilders
                        .post(uri)
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

    }

    @Test
    void getOne() throws Exception{
        URI uri = new URI("/v1/users/1");

        mockMvc.perform(MockMvcRequestBuilders
                .get(uri)).andExpect(status().is(200));

    }


}
