package com.MS.history.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserForm {

    private String firstName;
    private String lastName;
    private String sex;
    private String cpf;
    private String birthdate;
    private String email;

}
