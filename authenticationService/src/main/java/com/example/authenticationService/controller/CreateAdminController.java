package com.example.authenticationService.controller;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.example.authenticationService.Utils.Constants.ADMIN_ACCESS;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register/admin")
public class CreateAdminController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;

    @PostMapping
    @PreAuthorize(ADMIN_ACCESS)
    public AdminDetails registerAdmin(@RequestBody AdminDetails adminDetails)
    {
        return createAdminService.save(adminDetails);
    }
}
