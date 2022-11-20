package com.example.authenticationService.controller;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.CreateStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register/student")
public class CreateStudentController {

    @Autowired
    CreateStudentService createStudentService;

    @PostMapping
    public StudentDetails register(@RequestBody StudentDetails studentDetails) {
        return createStudentService.save(studentDetails);

    }
}
