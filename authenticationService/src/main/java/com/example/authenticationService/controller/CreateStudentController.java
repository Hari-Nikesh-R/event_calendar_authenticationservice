package com.example.authenticationService.controller;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.authenticationService.Utils.Constants.ADMIN_ACCESS;
import static com.example.authenticationService.Utils.Constants.STAFF_ACCESS;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register/student")
public class CreateStudentController {

    @Autowired
    RegisterService<StudentDetails> createStudentService;

    @PostMapping
    @PreAuthorize(ADMIN_ACCESS + " or " + STAFF_ACCESS)
    public StudentDetails registerStudent(@RequestBody StudentDetails studentDetails) {
        return createStudentService.save(studentDetails);

    }
}
