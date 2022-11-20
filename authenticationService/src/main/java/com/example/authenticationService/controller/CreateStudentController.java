package com.example.authenticationService.controller;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register/student")
public class CreateStudentController {

    @Autowired
    RegisterService<StudentDetails> createStudentService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public StudentDetails registerStudent(@RequestBody StudentDetails studentDetails) {
        return createStudentService.save(studentDetails);

    }
}
