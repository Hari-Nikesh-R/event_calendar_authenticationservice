package com.example.authenticationService.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "staff_detail")
public class StaffDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String mainHandlingSubject;
    private String careerDescription;
    private String rollNumber;
    private String designation;
    private String password;
    private String createdBy;
    private String passwordUpdatedBy;
    private String role = "STAFF";
}
