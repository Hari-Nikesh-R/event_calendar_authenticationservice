package com.example.authenticationService.controller;

import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.ADMIN_ACCESS;
import static com.example.authenticationService.Utils.Constants.STAFF_ACCESS;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    FetchInfoService<StudentDetails,Integer> fetchStudentInfo;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/student-info/all")
    @PreAuthorize(ADMIN_ACCESS+" or "+STAFF_ACCESS)
    public BaseResponse<List<StudentDetails>> getAllStudents(){
        List<StudentDetails> studentDetails = fetchStudentInfo.getAllInfo();
        if(Objects.nonNull(studentDetails)) {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true, "", studentDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(),HttpStatus.NO_CONTENT.value(),true,"No Students found",null);
    }
    @GetMapping(value = "/fetch-id")
    public BaseResponse<Integer> getStudentId(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ","");
        String email = jwtTokenUtil.getUsernameFromToken(token);
        Integer studentInfoId = fetchStudentInfo.getId(email);
        if(Objects.nonNull(studentInfoId)) {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true, "", studentInfoId);
        }
        return new BaseResponse<>("Something went wrong try again", HttpStatus.NO_CONTENT.value(),false,"Unable to fetch Id",null);
    }

}
