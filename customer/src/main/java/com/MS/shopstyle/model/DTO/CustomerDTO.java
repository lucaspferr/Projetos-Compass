package com.MS.shopstyle.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String sex;
    private String cpf;
    private String birthdate;
    private String email;
    private String password;
    private boolean active;

    public CustomerDTO(String teste, String testing, String masculino, String s, String s1, String s2, String s3, boolean b) {
    }
}
