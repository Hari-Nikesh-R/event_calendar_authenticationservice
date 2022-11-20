package com.example.authenticationService.controller;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.authenticationService.Utils.Constants.ADMIN_ACCESS;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register/staff")
public class CreateStaffController {

    @Autowired
    RegisterService<StaffDetails> createStaffService;

    @PostMapping
    @PreAuthorize(ADMIN_ACCESS)
    public StaffDetails registerStaff(@RequestBody StaffDetails staffDetails){
        return createStaffService.save(staffDetails);
    }
}
