package com.example.authenticationService.services.impl;

import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.StaffDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
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
        List<StaffDetails> staffDetails = staffDetailsInformation.findAll();
        for(StaffDetails staffDetail : staffDetails)
        {
            staffDetail.setPassword("");
        }
        return staffDetails;
    }

    @Override
    public Integer getId(String email) {
        Optional<Integer> optionalStaffId = staffDetailsInformation.fetchId(email);
        return optionalStaffId.orElse(null);
    }
    @Override
    public StaffDetails getInfoById(Integer id) {
        Optional<StaffDetails>  optionalStaffDetails = staffDetailsInformation.findById(id);
        return optionalStaffDetails.orElse(null);
    }

    @Override
    public String changePassword(UpdatePassword updatePassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        StaffDetails staffDetails = getInfoById(updatePassword.getId());
        if(Objects.nonNull(staffDetails)) {
            staffDetails.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
            staffDetailsInformation.save(staffDetails);
            return "Password Updated Successfully";
        }
        return "Failed to Update password";
    }
}
