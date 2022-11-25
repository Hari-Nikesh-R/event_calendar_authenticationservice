package com.example.authenticationService.controller;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StaffDetails;
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
@RequestMapping(value = "/register")
public class CreateController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;
    @Autowired
    RegisterService<StaffDetails> createStaffService;
    @Autowired
    RegisterService<StudentDetails> createStudentService;

    @PostMapping(value = "/admin")
    @PreAuthorize(ADMIN_ACCESS)
    public BaseResponse<AdminDetails> registerAdmin(@RequestBody AdminDetails adminDetails)
    {
        AdminDetails details = createAdminService.save(adminDetails);
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist", null);
        }


    }



    @PostMapping(value = "/staff")
    @PreAuthorize(ADMIN_ACCESS)
    public BaseResponse<StaffDetails> registerStaff(@RequestBody StaffDetails staffDetails){
        StaffDetails details = createStaffService.save(staffDetails);
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist",null);
        }
    }


    @PostMapping(value = "/student")
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
