package com.MS.shopstyle.service;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.Sex;
import com.MS.shopstyle.repository.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import javax.transaction.Transactional;
import javax.xml.bind.ValidationException;
import java.time.LocalDate;
import java.util.List;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    private ModelMapper modelMapper = new ModelMapper();

    public LocalDate dateConversor(String dateString){
        try{
            String[] dateArray = dateString.split("/");
            LocalDate date = LocalDate.parse(dateArray[2]+"-"+dateArray[1]+"-"+dateArray[0]);
            return date;
        }catch (Exception e){
            throw new IllegalStateException("Type a valid date with the pattern dd/mm/yyyy");
        }
    }

    public String dateConversor(LocalDate date) {
        String[] preDate = (date.toString()).split("-");
        String posDate = preDate[2]+"/"+preDate[1]+"/"+preDate[0];
        return posDate;
    }

    public String emailChecker(String email){
        if (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$")
                || email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]+[\\w]+[\\w]$")) return emailDuplicatedChecker(email);
        else throw new IllegalStateException("Type a valid email format only");
    }

    public String cpfChecker(String cpf) {
        try {
            if (cpf.matches("\\pN{3}.\\pN{3}.\\pN{3}-\\pN{2}")) return cpf;
            else throw new IllegalStateException("Invalid CPF");
        }catch (Exception e){throw new IllegalStateException("Invalid CPF");}
    }

    public String emailDuplicatedChecker(String email){
        List<Customer> customerList = customerRepository.findAll();
        for(Customer customer : customerList){if(customer.getEmail().matches(email)){throw new IllegalStateException("Email already being used");}}
        return email;
    }

    public Sex stringToSex(String sexString){
        sexString = sexString.toLowerCase();
        if (sexString.matches("masculino")) return Sex.MASCULINO;
        else if (sexString.matches("feminino")) return Sex.FEMININO;
            //else return null;
        else {throw new IllegalStateException("Type 'Masculino' or 'Feminino' only");}
    }

    public Customer dtoToUserConversor(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setSex(stringToSex(customerDTO.getSex()));
        customer.setCpf(cpfChecker(customerDTO.getCpf()));
        customer.setBirthdate(dateConversor(customerDTO.getBirthdate()));
        customer.setEmail(emailChecker(customerDTO.getEmail()));
        customer.setActive(customerDTO.getActive());
        if(!(customerDTO.getPassword()==null)) if(!(customerDTO.getPassword().isEmpty())) customer.setPassword(passwordEncrypter(customerDTO.getPassword()));
        return customer;
    }

//    public CustomerDTO userToDtoConversor(Customer customer) {
//        CustomerDTO customerDTO = new CustomerDTO();
//
//        customerDTO.setId(customer.getId());
//        customerDTO.setFirstName(customer.getFirstName());
//        customerDTO.setLastName(customer.getLastName());
//        customerDTO.setSex(customer.getSex().toString());
//        customerDTO.setCpf(cpfChecker(customer.getCpf()));
//        customerDTO.setBirthdate(dateConversor(customer.getBirthdate()));
//        customerDTO.setEmail(customer.getEmail());
//        customerDTO.setPassword(customer.getPassword());
//        customerDTO.setActive(customer.isActive());
//
//        return customerDTO;
//    }

    public String passwordEncrypter(String prePassword){return passwordEncoder.encode(prePassword);}

    public Customer dtoToCustomer(CustomerDTO customerDTO){

        Customer customer = modelMapper.map(customerDTO, Customer.class);
        customer.setBirthdate(dateConversor(customerDTO.getBirthdate()));
        customer.setSex(stringToSex(customerDTO.getSex()));
        customer.setPassword(passwordEncrypter(customerDTO.getPassword()));
        customer.setCpf(cpfChecker(customerDTO.getCpf()));

        return customer;
    }

    public CustomerDTO customerToDTO(Customer customer){
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);
        return customerDTO;
    }

    public CustomerDTO findOne(Long id){
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new IllegalStateException("User with the ID "+id+" doesn't exist."));
        return customerToDTO(customer);
    }

    @Transactional
    public Customer updateCustomer(Long idCustomer, CustomerDTO customerDTO) {
        Customer customer2 = customerRepository.findById(idCustomer).orElseThrow(() -> new IllegalStateException("User with the ID "+idCustomer+" doesn't exist."));
        Customer customer = dtoToCustomer(customerDTO);
        if(!(customer2.getEmail().matches(customer.getEmail()))) {customer.setEmail(emailChecker(customer.getEmail()));}
        customer.setId(customer2.getId());

        return customerRepository.save(customer);
    }

    public Customer createCustomer(CustomerDTO customerDTO){
        Customer customer = dtoToCustomer(customerDTO);
        return customerRepository.save(customer);
    }

}
