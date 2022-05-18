package com.MS.shopstyle.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private Long id;
    @NotNull @NotBlank @Size(min = 3)
    private String firstName;
    @NotNull @NotBlank @Size(min = 3)
    private String lastName;
    @NotNull @NotBlank
    private String sex;
    @NotNull @NotBlank
    private String cpf;
    @NotNull @NotBlank
    private String birthdate;
    @NotNull @NotBlank
    private String email;
    @NotNull @NotBlank @Size(min = 8)
    private String password;
    @NotNull
    private Boolean active;

    public CustomerDTO(String teste, String testing, String masculino, String s, String s1, String s2, String s3, boolean b) {
    }
}
