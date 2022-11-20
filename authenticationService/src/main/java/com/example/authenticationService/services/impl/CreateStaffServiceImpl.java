package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.repository.StaffDetailsRepository;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateStaffServiceImpl implements RegisterService<StaffDetails> {

    @Autowired
    StaffDetailsRepository staffDetailsInformation;

    @Override
    public StaffDetails save(StaffDetails staffDetails) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(staffDetails.getPassword());
        staffDetails.setPassword(password);
       return staffDetailsInformation.save(staffDetails);
    }
}
