package com.example.authenticationService.model;

import lombok.Data;
import org.springframework.security.core.userdetails.User;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "student_details")
public class StudentDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String profilePicture;
    private String collegeRollNumber;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date dob;
    private String phoneNumber;
    private String alterPhoneNumber;
    private double CGPA;
    private double SSLCMark;
    private double HSCMark;
    private String department;
    private String section;
    private String RegisterNumber;
    private String password;
    private String createdBy;
    private String passwordUpdatedBy;
    private String role = "STUDENT";
    private String academicYear;
}
