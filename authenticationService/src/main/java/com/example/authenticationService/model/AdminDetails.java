package com.example.authenticationService.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "adminDetails")
public class AdminDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String organization;
    private String phoneNumber;
    private String password;
    private String organizationalAddress;
    private String role = "ADMIN";
}
