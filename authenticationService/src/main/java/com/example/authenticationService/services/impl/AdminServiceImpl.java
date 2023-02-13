package com.example.authenticationService.services.impl;

import com.example.authenticationService.Utils.Utility;
import com.example.authenticationService.dtos.Authority;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.MAIL_URL;

@Service
public class AdminServiceImpl implements RegisterService<AdminDetails>, FetchInfoService<AdminDetails, Integer>, AdminService {
    @Autowired
    AdminDetailsRepository adminDetailsRepository;
    @Autowired
    GenerateResetPassCode generateResetPassCode;
    @Autowired
    RestTemplate restTemplate;

    private Map<String, String> generatedCode = new HashMap<>();

    @Override
    public AdminDetails save(AdminDetails adminDetails) {
            Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(adminDetails.getEmail());
            if (optionalAdminDetails.isPresent()) {
                return null;
            }
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            String password = bCryptPasswordEncoder.encode(adminDetails.getPassword());
            adminDetails.setPassword(password);
            return adminDetailsRepository.save(adminDetails);
    }
    @Override
    public BaseResponse<String> sendCodeToMail(String emailId) {
        generatedCode.put(emailId,generateResetPassCode.generateCode());
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setSubject("Email Confirmation mail");
        emailDetails.setRecipient(emailId);
        emailDetails.setMsgBody("Confirmation code for the creating account is : "+generatedCode.get(emailId));
        String response = restTemplate.postForEntity(MAIL_URL + "/passcode", emailDetails, String.class).getBody();
        return new BaseResponse<>("", HttpStatus.OK.value(), true, "", response);
    }

    @Override
    public BaseResponse<String> verifyCode(String code, AdminDetails adminDetails) {
            if (code != null) {
                if (code.equals(generatedCode.get(adminDetails.getEmail()))) {
                        save(adminDetails);
                        return new BaseResponse<>("Code verified and Registered successful", HttpStatus.OK.value(), true, "", "Success");
                    } else {
                        return new BaseResponse<>("Wrong Code", HttpStatus.FORBIDDEN.value(), false, "Cannot create account", "Invalid Code");
                    }
                }
        return new BaseResponse<>("Code not verified", HttpStatus.FORBIDDEN.value(), false, "", "Not Verified");
    }
    @Override
    public String updateAuthority(Authority authority) {
        Optional<AdminDetails> optionalAdminDetails =adminDetailsRepository.findByEmail(authority.getEmail());
        if(optionalAdminDetails.isPresent())
        {
            optionalAdminDetails.get().setAuthority(authority.isAuthorized());
            adminDetailsRepository.save(optionalAdminDetails.get());
        }
        return "Changed Authority";
    }

    @Override
    public Boolean isAuthorizedUser(String email) {
        return null;
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
        if(optionalAdminDetails.isPresent())
        {
            optionalAdminDetails.get().setPassword("");
            return optionalAdminDetails.get();
        }
        return null;
    }

    @Override
    public AdminDetails getInfoByEmail(String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        return optionalAdminDetails.orElse(null);
    }

    @Override
    public String changePassword(UpdatePassword updatePassword) {
        generatedCode.remove(updatePassword.getEmail());
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        AdminDetails adminDetails = getInfoById(updatePassword.getId());
        if(Objects.nonNull(adminDetails)) {
            adminDetails.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
            adminDetailsRepository.save(adminDetails);
            return UPDATE_PASSWORD;
        }
        return null;
    }

    @Override
    public String forgotPasswordReset(UpdatePassword updatePassword) {
        BaseResponse<Map<String,String>> baseResponse = restTemplate.exchange(MAIL_URL + "/admin/fetch/forgot-password/code",HttpMethod.GET,null,new ParameterizedTypeReference<BaseResponse<Map<String,String>>>() {}).getBody();
        Map<String,String> forgotPassCode = baseResponse.getValue();
        if(updatePassword.getCode().equals(forgotPassCode.get(updatePassword.getEmail()))) {
            BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
            Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(updatePassword.getEmail());
            if(optionalAdminDetails.isPresent())
            {
                optionalAdminDetails.get().setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
                adminDetailsRepository.save(optionalAdminDetails.get());
                return UPDATE_PASSWORD;
            }

        }
        return null;
    }

    @Override
    public AdminDetails updateProfile(AdminDetails details, String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        if(optionalAdminDetails.isPresent()) {
            details.setId(optionalAdminDetails.get().getId());
            details.setEmail(optionalAdminDetails.get().getEmail());
            details.setPassword(optionalAdminDetails.get().getPassword());
            BeanUtils.copyProperties(details, optionalAdminDetails.get());
            return adminDetailsRepository.save(optionalAdminDetails.get());
        }
        return null;
    }

    @Override
    public Boolean validateByEmail(String email) {
        Optional<AdminDetails> optionalAdminDetails = adminDetailsRepository.findByEmail(email);
        return optionalAdminDetails.isPresent();
    }

}
