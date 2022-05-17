package com.MS.shopstyle.service;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.Sex;
import com.MS.shopstyle.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;


@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public LocalDate dateConversor(String dateString){
        try{
            String[] dateArray = dateString.split("/");
            LocalDate date = LocalDate.parse(dateArray[2]+"-"+dateArray[1]+"-"+dateArray[0]);
            return date;
        }catch (Exception e){
            return null;
        }
    }

    public String dateConversor(LocalDate date) {
        String[] preDate = (date.toString()).split("-");
        String posDate = preDate[2]+"/"+preDate[1]+"/"+preDate[0];
        return posDate;
    }

    public String emailChecker(String email){
        try {
            if (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$") && !emailDuplicatedChecker(email))
                return email;
            else if (email.matches("^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]+[\\w]+[\\w]$") && !emailDuplicatedChecker(email))
                return email;
            else return null;
        }catch (Exception e){return null;}
    }

    public String cpfChecker(String cpf) {
        try {
            if (cpf.matches("\\pN{3}.\\pN{3}.\\pN{3}-\\pN{2}")) return cpf;
            else return null;
        }catch (Exception e){return null;}
    }

    public boolean emailDuplicatedChecker(String email){
        List<Customer> customerList = customerRepository.findAll();
        for(Customer customer : customerList){if(customer.getEmail().matches(email)) return true;}
        return false;
    }

    public Sex stringToSex(String sexString){
        try{
            sexString = sexString.toLowerCase();
            if (sexString.matches("masculino")) return Sex.MASCULINO;
            else if (sexString.matches("feminino")) return Sex.FEMININO;
                //else return null;
            else {
                throw new IllegalStateException("Digite apenas masculino ou feminino.");
            }
        }catch (NullPointerException e){
            return null;}
    }

    public Customer dtoToUserConversor(CustomerDTO customerDTO) {
        Customer customer = new Customer();

        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setSex(stringToSex(customerDTO.getSex()));
        customer.setCpf(cpfChecker(customerDTO.getCpf()));
        customer.setBirthdate(dateConversor(customerDTO.getBirthdate()));
        customer.setEmail(emailChecker(customerDTO.getEmail()));
        customer.setActive(customerDTO.isActive());
        if(!(customerDTO.getPassword()==null)) if(!(customerDTO.getPassword().isEmpty())) customer.setPassword(passwordEncrypter(customerDTO.getPassword()));
        return customer;
    }

    public CustomerDTO userToDtoConversor(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setId(customer.getId());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setSex(customer.getSex().toString());
        customerDTO.setCpf(cpfChecker(customer.getCpf()));
        customerDTO.setBirthdate(dateConversor(customer.getBirthdate()));
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setActive(customer.isActive());

        return customerDTO;
    }

    public String passwordEncrypter(String prePassword){
        if(prePassword.length()>=8) return passwordEncoder.encode(prePassword);
        throw new IllegalStateException("Senha precisa ter um tamanho de no mínimo 8 carácteres.");

    }

    @Transactional
    public void updateCustomer(Long idCustomer, Customer customer) {
        Customer customer2 = customerRepository.findById(idCustomer).orElseThrow(() -> new IllegalStateException("Usuário com o ID "+idCustomer+" não existente."));
        customer2.setFirstName(customer.getFirstName());
        customer2.setLastName(customer.getLastName());
        customer2.setSex(customer.getSex());
        customer2.setCpf(customer.getCpf());
        customer2.setBirthdate(customer.getBirthdate());
        customer2.setEmail(customer.getEmail());
        customer2.setPassword(customer.getPassword());
        customer2.setActive(customer.isActive());

        customerRepository.save(customer2);
    }
}
