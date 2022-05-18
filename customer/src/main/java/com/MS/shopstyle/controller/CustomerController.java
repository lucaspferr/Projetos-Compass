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
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private AuthTokenService tokenService;

    @GetMapping("users/{idCustomer}")
    public CustomerDTO listCustomer(@PathVariable Long idCustomer){
        Customer customer = customerRepository.findById(idCustomer).orElseThrow(() -> new IllegalStateException("Usuario com o ID "+idCustomer+" não existe."));
        return customerService.userToDtoConversor(customer);
    }

    @PostMapping("users")
    public void saveUser(@RequestBody CustomerDTO customerDTO){
        customerRepository.save(customerService.dtoToUserConversor(customerDTO));
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
    public ResponseEntity<?> updateCustomer(@PathVariable Long idCustomer, @RequestBody @Valid CustomerDTO customerDTO, Errors errors){
        if(errors.hasErrors()) customerService.validationErrors(errors.toString());
        Customer customer = customerService.updateCustomer(idCustomer, customerDTO);
        return new ResponseEntity(customer, HttpStatus.CREATED);
    }
}
