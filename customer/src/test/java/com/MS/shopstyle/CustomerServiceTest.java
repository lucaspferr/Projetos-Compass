package com.MS.shopstyle;

import com.MS.shopstyle.model.Customer;
import com.MS.shopstyle.model.DTO.CustomerDTO;
import com.MS.shopstyle.model.Sex;
import com.MS.shopstyle.service.CustomerService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
public class CustomerServiceTest {


    private CustomerService customerService;

    @Autowired
    public CustomerServiceTest(CustomerService customerService) {
        this.customerService = customerService;
    }


    public CustomerDTO geradorDTO(){
        CustomerDTO usuarioDTO = new CustomerDTO("Teste","Testing","Masculino","111.111.111-11","19/04/1997","teste@teste.com","123456",true);
        return usuarioDTO;
    }

    public Customer gerador(){
        Customer usuario = new Customer("Teste","Testing", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste@teste.com","123456",true);
        return usuario;
    }

    public List<Customer> listEmailrepetido = Arrays.asList(
            new Customer("Teste1","Testing1", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste@teste.com","123456",true),
            new Customer("Teste2","Testing2", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste@teste.com","123456",true),
            new Customer("Teste3","Testing3", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste@teste.com","123456",true)
    );

    public List<Customer> listEmailsDiferentes = Arrays.asList(
            new Customer("Teste1","Testing1", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste1@teste.com","123456",true),
            new Customer("Teste2","Testing2", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste2@teste.com","123456",true),
            new Customer("Teste3","Testing3", Sex.MASCULINO,"111.111.111-11", LocalDate.of(1997,04,19),"teste3@teste.com","123456",true)
    );

    @Test
    public void conversorLocalDateparaFormatoBrasileiro(){
        //deve enviar localdate ano-mes-dia e retornar string dia/mes/ano. LocalDate sera puxado direto do banco
        Assertions.assertEquals("19/04/1997",customerService.dateConversor(gerador().getBirthdate()));
    }

    @Test
    public void conversorFormatoBrasileiroparaLocalDate(){
        //deve enviar string dia/mes/ano e retornar localdate ano-mes-dia. Retorna nulo caso nao consiga realizar a conversao
        Assertions.assertEquals(LocalDate.of(1997,04,19),customerService.dateConversor(geradorDTO().getBirthdate()));
        Assertions.assertNull(customerService.dateConversor("aa/aa/aaaa"));
        Assertions.assertNull(customerService.dateConversor("19/4/1997"));
        Assertions.assertNull(customerService.dateConversor("19/04/97"));
        Assertions.assertNull(customerService.dateConversor("aAaAaAaAa"));
        Assertions.assertNull(customerService.dateConversor(""));
    }

    @Test
    public void checarSeEmailEhValido(){
        //enviar uma string e verificar se eh valido
        //Teste com .com.br
        Assertions.assertEquals("lucas@compasso.com.br",customerService.emailChecker("lucas@compasso.com.br"));
        //Teste com .com
        Assertions.assertEquals(geradorDTO().getEmail(),customerService.emailChecker(geradorDTO().getEmail()));
        Assertions.assertNull(customerService.emailChecker("lucas!compasso.com"));
        Assertions.assertNull(customerService.emailChecker("@compasso.com"));
        Assertions.assertNull(customerService.emailChecker("lucas@compassocom"));
    }

    @Test
    public void checarSeCPFehValido(){
        //enviar um string com o cpf e verificar se esta no formato xxx.xxx.xxx-xx
        Assertions.assertEquals("111.111.111-11",customerService.cpfChecker(geradorDTO().getCpf()));
        Assertions.assertNull(customerService.cpfChecker("000.000.000-0A"));
        Assertions.assertNull(customerService.cpfChecker("000.000.00A-00"));
        Assertions.assertNull(customerService.cpfChecker("000.00A.000-00"));
        Assertions.assertNull(customerService.cpfChecker("00A.000.000-00"));
        Assertions.assertNull(customerService.cpfChecker("11111111111"));
    }

    @Test
    public void checarSeEmailEhDuplicado(){
        //envia uma string de email e busca no banco se existe algum outro exemplo
        String email = "teste@teste.com";
        //Assertions.assertEquals(false,customerService.emailDuplicatedChecker(email));
        //Assertions.assertEquals(true,customerService.emailDuplicatedChecker(email));
        //Codigo foi refatorado, removendo a necessidade do envio de String, trocando para o uso do .findall()
    }

    @Test
    public void conveterStringParaSexo(){
        //envia uma String e converte para enum Sex
        Assertions.assertEquals(Sex.MASCULINO,customerService.stringToSex("Masculino"));
        Assertions.assertEquals(Sex.MASCULINO,customerService.stringToSex("masculino"));
        Assertions.assertEquals(Sex.MASCULINO,customerService.stringToSex("MaScuLiNo"));
        Assertions.assertNull(customerService.stringToSex("eh pra ser null"));
        Assertions.assertEquals(Sex.FEMININO, customerService.stringToSex("Feminino"));
        Assertions.assertEquals(Sex.FEMININO, customerService.stringToSex("feminino"));
        Assertions.assertEquals(Sex.FEMININO, customerService.stringToSex("FEmiNiNo"));
    }

    @Test
    public void converterDTOparaNormal(){
        //Envia um CustomerDTO e converte para formato Customer
        Customer customer = new Customer();
        customer = customerService.dtoToUserConversor(geradorDTO());

        Assertions.assertEquals(gerador().getFirstName(),customer.getFirstName());
        Assertions.assertEquals(gerador().getLastName(),customer.getLastName());
        Assertions.assertEquals(gerador().getCpf(),customer.getCpf());
        Assertions.assertEquals(gerador().getBirthdate(),customer.getBirthdate());
        Assertions.assertEquals(gerador().getEmail(),customer.getEmail());
    }

    @Test
    public void converterNormalparaDTO(){
        //Envia um Customer e covnerte para o formato CustomerDTO
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO = customerService.userToDtoConversor(gerador());

        Assertions.assertEquals(geradorDTO().getFirstName(),customerDTO.getFirstName());
        Assertions.assertEquals(geradorDTO().getLastName(),customerDTO.getLastName());
        Assertions.assertEquals(geradorDTO().getCpf(),customerDTO.getCpf());
        Assertions.assertEquals(geradorDTO().getBirthdate(),customerDTO.getBirthdate());
        Assertions.assertEquals(geradorDTO().getEmail(),customerDTO.getEmail());
    }

    @Test
    public void senhaEhEncriptografada(){
        //envia uma string com a senha 'normal' e retorna a senha encriptografada
        Assertions.assertNotEquals("12345678",customerService.passwordEncrypter("12345678"));
    }

}