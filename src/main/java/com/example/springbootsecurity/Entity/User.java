package com.example.springbootsecurity.Entity;


import jakarta.persistence.*;
import lombok.*;



@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "user_sequence")
    @SequenceGenerator(name = "user_sequence",sequenceName = "user_sequence",allocationSize = 1)
    private long id;
    private String firstName;
    private String LastName;
    private  String Email;
    @Column(length = 60)
    private String password;
    private String role;
    private boolean enable = false;
}
