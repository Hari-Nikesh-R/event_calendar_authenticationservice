package com.example.authenticationService.controller;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register/staff")
public class CreateStaffController {

    @Autowired
    RegisterService<StaffDetails> createStaffService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public StaffDetails registerStaff(StaffDetails staffDetails){
        return createStaffService.save(staffDetails);
    }
}
