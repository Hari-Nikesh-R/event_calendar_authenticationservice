package com.example.authenticationService.services.impl;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.services.AdminService;
import com.example.authenticationService.services.GenerateResetPassCode;
import com.example.authenticationService.dtos.EmailDetails;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
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
public class AdminServiceImpl implements RegisterService<AdminDetails>, FetchInfoService<AdminDetails,Integer>, AdminService {
    @Autowired
    AdminDetailsRepository adminDetailsRepository;
    @Autowired
    GenerateResetPassCode generateResetPassCode;
    @Autowired
    RestTemplate restTemplate;

    private String generatedCode="";

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
    public BaseResponse<String> resetPassword(Integer id) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findById(id);
        EmailDetails emailDetails = new EmailDetails();
        if(optionalAdminDetails.isPresent()) {
            generatedCode = generateResetPassCode.generateCode();
            emailDetails.setCode(generatedCode);
            emailDetails.setRecipient(optionalAdminDetails.get().getEmail());
            emailDetails.setMsgBody("You code for Reset Password: " + emailDetails.getCode());
            emailDetails.setSubject("LEAVE TRACKER - Reset Password");
        }
        String response = restTemplate.postForEntity(MAIL_URL + "/passcode", emailDetails, String.class).getBody();
        return new BaseResponse<>("", HttpStatus.OK.value(), true,"",response);
    }

    @Override
    public BaseResponse<String> verifyCode(Integer id, String code) {
       Optional<AdminDetails> optionalAdminDetails =  adminDetailsRepository.findById(id);
       if(optionalAdminDetails.isPresent())
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
    public List<AdminDetails> getAllInfo() {
        return adminDetailsRepository.findAll();
    }

    @Override
    public Integer getId(String email) {
        Optional<Integer> optionalAdminId = adminDetailsRepository.fetchId(email);
        return optionalAdminId.orElse(null);
    }

    @Override
    public AdminDetails getInfoById(Integer id) {
        Optional<AdminDetails>  optionalAdminDetails = adminDetailsRepository.findById(id);
        return optionalAdminDetails.orElse(null);
    }

    @Override
    public String changePassword(UpdatePassword updatePassword,Boolean isResetPassword) {
        generatedCode = "";
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        AdminDetails adminDetails = getInfoById(updatePassword.getId());
        if(Objects.nonNull(adminDetails)) {
            adminDetails.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
            adminDetailsRepository.save(adminDetails);
            return UPDATE_PASSWORD;
        }
        return UPDATE_PASSWORD_FAILED;
    }

    @Override
    public AdminDetails updateProfile(AdminDetails details, Integer id) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findById(id);
        if(optionalAdminDetails.isPresent()) {
            details.setId(optionalAdminDetails.get().getId());
            BeanUtils.copyProperties(details, optionalAdminDetails.get());
            return adminDetailsRepository.save(optionalAdminDetails.get());
        }
        return null;
    }
}
