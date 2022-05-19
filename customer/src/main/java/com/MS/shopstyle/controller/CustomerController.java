package com.MS.shopstyle.controller;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.CustomerLogin;
import com.MS.shopstyle.model.DTO.TokenDTO;
import com.MS.shopstyle.repository.CustomerRepository;
import com.MS.shopstyle.service.AuthTokenService;
import com.MS.shopstyle.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("v1")


public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthTokenService tokenService;

    @GetMapping("users/{idCustomer}")
    public ResponseEntity<?> listCustomer(@PathVariable Long idCustomer){
        CustomerDTO customerDTO = customerService.findOne(idCustomer);
        return ResponseEntity.ok().body(customerDTO);
    }

    @PostMapping("users")
    public ResponseEntity<?> saveUser(@RequestBody @Valid CustomerDTO customerDTO){
        Customer customer = customerService.createCustomer(customerDTO);
        return new ResponseEntity(customer, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid CustomerLogin customerLogin){
        UsernamePasswordAuthenticationToken credentials = customerLogin.dataConversor();
        //converte o objeto para login e senha
        try {
            Authentication authentication = authManager.authenticate(credentials);

            String token = tokenService.tokenGenerator(authentication);

            return ResponseEntity.ok(new TokenDTO(token, "Bearer"));
        }catch (AuthenticationException e){return ResponseEntity.badRequest().body("Problema de autenticação.");}

    }

    @PutMapping("users/{idCustomer}")
    public ResponseEntity<?> updateCustomer(@PathVariable Long idCustomer, @RequestBody @Valid CustomerDTO customerDTO){
        Customer customer = customerService.updateCustomer(idCustomer, customerDTO);
        return new ResponseEntity(customer, HttpStatus.CREATED);
    }
}
