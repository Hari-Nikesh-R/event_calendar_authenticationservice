package com.example.authenticationService.controller;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/register/#admin")
public class CreateAdminController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public AdminDetails registerAdmin(AdminDetails adminDetails)
    {
        return createAdminService.save(adminDetails);
    }
}
