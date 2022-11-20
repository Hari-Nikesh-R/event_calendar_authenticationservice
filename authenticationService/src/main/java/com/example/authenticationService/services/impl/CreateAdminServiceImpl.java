package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateAdminServiceImpl implements RegisterService<AdminDetails> {
    @Autowired
    AdminDetailsRepository adminDetailsRepository;

    @Override
    public AdminDetails save(AdminDetails adminDetails) {
        return adminDetailsRepository.save(adminDetails);
    }
}
