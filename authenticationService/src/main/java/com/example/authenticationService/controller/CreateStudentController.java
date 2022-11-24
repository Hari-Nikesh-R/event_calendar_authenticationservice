package com.example.authenticationService.controller;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

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
    public BaseResponse<StudentDetails> registerStudent(@RequestBody StudentDetails studentDetails) {
        StudentDetails details = createStudentService.save(studentDetails);
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist",null);
        }

    }
}
