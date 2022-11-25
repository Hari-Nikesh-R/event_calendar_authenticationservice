package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.repository.StaffDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreateStaffServiceImpl implements RegisterService<StaffDetails>, FetchInfoService<StaffDetails,Integer> {

    @Autowired
    StaffDetailsRepository staffDetailsInformation;

    @Override
    public StaffDetails save(StaffDetails staffDetails) {
        Optional<StaffDetails> optionalStaffDetails = staffDetailsInformation.findByEmail(staffDetails.getEmail());
        if(optionalStaffDetails.isPresent())
        {
            return null;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(staffDetails.getPassword());
        staffDetails.setPassword(password);
       return staffDetailsInformation.save(staffDetails);
    }

    @Override
    public List<StaffDetails> getAllInfo() {
        return null;
    }

    @Override
    public Integer getId(String email) {
        return null;
    }


    @Override
    public StaffDetails getInfoById(Integer id) {
        return null;
    }
}
