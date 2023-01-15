package com.example.authenticationService.controller;

import com.example.authenticationService.Utils.Utility;
import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.services.AdminService;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.*;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/register")
public class CreateController {

    @Autowired
    RegisterService<AdminDetails> createAdminService;

    @Autowired
    FetchInfoService<AdminDetails,Integer> adminDetailsIntegerFetchInfoService;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    AdminService adminService;

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @PostMapping(value = ADMIN_URL)
    public BaseResponse<AdminDetails> registerAdmin(@RequestBody AdminDetails adminDetails, @RequestHeader(AUTHORIZATION) String token)
    {
       AdminDetails details=null;
        if(Utility.validatePassword(adminDetails.getPassword()) && Utility.validateEmailId(adminDetails.getEmail())) {
            details = createAdminService.save(adminDetails);
        }
        else{
            return new BaseResponse<>(HttpStatus.NOT_ACCEPTABLE.getReasonPhrase(),HttpStatus.NOT_ACCEPTABLE.value(), false,"Invalid Format",null);
        }
        if(Objects.nonNull(details)){
            return new BaseResponse<>(HttpStatus.CREATED.getReasonPhrase(), HttpStatus.OK.value(),true,"",details);
        }
        else{
            return new BaseResponse<>(HttpStatus.ALREADY_REPORTED.getReasonPhrase(), HttpStatus.ALREADY_REPORTED.value(), false, "User Already Exist", null);
        }
    }


    private HttpEntity<String> setTokenInHeaders(String token){
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.set(AUTHORIZATION,token);
        return new HttpEntity<>(httpHeaders);
    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
