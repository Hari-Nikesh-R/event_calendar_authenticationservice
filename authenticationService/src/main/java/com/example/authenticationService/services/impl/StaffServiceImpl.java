package com.example.authenticationService.services.impl;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.EmailDetails;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.repository.StaffDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.GenerateResetPassCode;
import com.example.authenticationService.services.RegisterService;
import com.example.authenticationService.services.StaffService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.example.authenticationService.Utils.Constants.UPDATE_PASSWORD;
import static com.example.authenticationService.Utils.Constants.UPDATE_PASSWORD_FAILED;
import static com.example.authenticationService.Utils.Urls.MAIL_URL;

@Service
public class StaffServiceImpl implements RegisterService<StaffDetails>, FetchInfoService<StaffDetails,Integer>, StaffService {

    @Autowired
    StaffDetailsRepository staffDetailsInformation;
    @Autowired
    GenerateResetPassCode generateResetPassCode;
    @Autowired
    RestTemplate restTemplate;

    private String generatedCode = "";

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
    public BaseResponse<String> resetPassword(Integer id) {
        Optional<StaffDetails> optionalStaffDetails = staffDetailsInformation.findById(id);
        EmailDetails emailDetails = new EmailDetails();
        if(optionalStaffDetails.isPresent()) {
            generatedCode = generateResetPassCode.generateCode();
            emailDetails.setCode(generatedCode);
            emailDetails.setRecipient(optionalStaffDetails.get().getEmail());
            emailDetails.setMsgBody("You code for Reset Password: " + emailDetails.getCode());
            emailDetails.setSubject("LEAVE TRACKER - Reset Password");
        }
        String response = restTemplate.postForEntity(MAIL_URL + "/passcode", emailDetails, String.class).getBody();
        return new BaseResponse<>("", HttpStatus.OK.value(), true,"",response);
    }

    @Override
    public BaseResponse<String> verifyCode(Integer id, String code) {
        Optional<StaffDetails> optionalStaffDetails =  staffDetailsInformation.findById(id);
        if(optionalStaffDetails.isPresent())
        {
            if(code!=null) {
                if (code.equals(generatedCode)) {
                    return new BaseResponse<>("Code verified", HttpStatus.OK.value(), true, "", "Success");
                }
            }
        }
        return new BaseResponse<>("Code not verified",HttpStatus.FORBIDDEN.value(), false,"","Not Verified");
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
    public String changePassword(UpdatePassword updatePassword,Boolean isResetPassword) {
        generatedCode = "";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        StaffDetails staffDetails = getInfoById(updatePassword.getId());
        if(Objects.nonNull(staffDetails)) {
            staffDetails.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
            staffDetailsInformation.save(staffDetails);
            return UPDATE_PASSWORD;
        }
        return UPDATE_PASSWORD_FAILED;
    }

    @Override
    public StaffDetails updateProfile(StaffDetails details, Integer id) {
        Optional<StaffDetails> optionalStaffDetails = staffDetailsInformation.findById(id);
        if(optionalStaffDetails.isPresent()) {
            details.setId(optionalStaffDetails.get().getId());
            BeanUtils.copyProperties(details, optionalStaffDetails.get());
            return staffDetailsInformation.save(optionalStaffDetails.get());
        }
        return null;
    }
}
