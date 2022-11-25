package com.example.authenticationService.controller;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/student")
public class StudentController {

    @Autowired
    FetchInfoService<StudentDetails,Integer> studentDetailsFetchInfoService;
    @GetMapping(value = "/info/{id}")
    public BaseResponse<StudentDetails> getStudentDetail(@PathVariable("id") Integer id){
        StudentDetails studentDetails = studentDetailsFetchInfoService.getInfoById(id);
        if(Objects.nonNull(studentDetails))
        {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true,"",studentDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(), HttpStatus.NO_CONTENT.value(), false,"No Student Found",null);
    }
}
