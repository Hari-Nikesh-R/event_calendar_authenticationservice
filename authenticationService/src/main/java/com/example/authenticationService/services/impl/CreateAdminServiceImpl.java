package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreateAdminServiceImpl implements RegisterService<AdminDetails>, FetchInfoService<AdminDetails,Integer> {
    @Autowired
    AdminDetailsRepository adminDetailsRepository;

    @Override
    public AdminDetails save(AdminDetails adminDetails) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(adminDetails.getEmail());
        if(optionalAdminDetails.isPresent())
        {
            return null;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(adminDetails.getPassword());
        adminDetails.setPassword(password);
        return adminDetailsRepository.save(adminDetails);
    }

    @Override
    public List<AdminDetails> getAllInfo() {
        return adminDetailsRepository.findAll();
    }

    @Override
    public Integer getId(String email) {
        return null;
    }


    @Override
    public AdminDetails getInfoById(Integer id) {
        return null;
    }
}
