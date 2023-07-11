package com.example.springbootsecurity.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private String firstName;
    private String LastName;
    private  String Email;
    private String password;
    private String confirmPassword;


}
