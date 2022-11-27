package com.example.authenticationService.controller;

import com.example.authenticationService.Utils.Utility;
import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.RegisterService;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register")
public class CreateController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;
    @Autowired
    RegisterService<StaffDetails> createStaffService;
    @Autowired
    RegisterService<StudentDetails> createStudentService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @PostMapping(value = ADMIN_URL)
    @PreAuthorize(ADMIN_ACCESS)
    public BaseResponse<AdminDetails> registerAdmin(@RequestBody AdminDetails adminDetails)
    {
       AdminDetails details=null;

        if(Utility.validatePassword(adminDetails.getPassword()) && Utility.validateEmailId(adminDetails.getEmail())) {
            details = createAdminService.save(adminDetails);
        }
        else{
            return new BaseResponse<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE.value(), false,"Invalid Format",null);
        }

        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist", null);
        }


    }



    @PostMapping(value = STAFF_URL)
    @PreAuthorize(ADMIN_ACCESS)
    public BaseResponse<StaffDetails> registerStaff(@RequestBody StaffDetails staffDetails,@RequestHeader(AUTHORIZATION) String token){
        staffDetails.setCreatedBy(jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ","")));
        StaffDetails details=null;
        if(Utility.validatePassword(staffDetails.getPassword()) && Utility.validateEmailId(staffDetails.getEmail())) {
            details = createStaffService.save(staffDetails);
        }
        else {
            return new BaseResponse<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(), HttpStatus.NOT_ACCEPTABLE.value(), false, "Invalid Format", null);
        }
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist",null);
        }
    }


    @PostMapping(value = STUDENT_URL)
    @PreAuthorize(ADMIN_ACCESS + " or " + STAFF_ACCESS)
    public BaseResponse<StudentDetails> registerStudent(@RequestBody StudentDetails studentDetails,@RequestHeader(AUTHORIZATION) String token) {
        studentDetails.setCreatedBy(jwtTokenUtil.getUsernameFromToken(token.replace("Bearer ","")));
        StudentDetails details=null;
        if(Utility.validatePassword(studentDetails.getPassword()) && Utility.validateEmailId(studentDetails.getEmail())) {
            details = createStudentService.save(studentDetails);
        }
        else{
            return new BaseResponse<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE.value(), false,"Invalid Format",null);
        }
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist",null);
        }

    }
}
