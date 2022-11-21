package com.example.authenticationService.controller;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.ADMIN_ACCESS;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register/admin")
public class CreateAdminController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;

    @PostMapping
    @PreAuthorize(ADMIN_ACCESS)
    public BaseResponse<AdminDetails> registerAdmin(@RequestBody AdminDetails adminDetails)
    {
        AdminDetails details = createAdminService.save(adminDetails);
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",adminDetails);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist",adminDetails);
        }


    }
}
