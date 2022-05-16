package com.MS.shopstyle.config.security;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional <Customer> customer = customerRepository.findByEmail(username);
        if(customer.isPresent()) return customer.get();
        throw new UsernameNotFoundException("Dados inválidos.");
    }



}
