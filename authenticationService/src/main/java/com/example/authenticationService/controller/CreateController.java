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
    @PostMapping(value = USER_URL)
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
    @PostMapping(value = "/verify/{code}")
    public BaseResponse<String> verifyRegistration(@PathVariable("code") String code,@RequestHeader(AUTHORIZATION) String token, @RequestBody AdminDetails adminDetails)
    {
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL+"/admin/fetch-id",HttpMethod.GET,entity,Integer.class).getBody();
        if(id==-1) {
            return new BaseResponse<>("User Not Found", HttpStatus.NO_CONTENT.value(), false,"Could not find Id",null);
        }
        return adminService.verifyCode(id,code,adminDetails);
    }

    @PostMapping
    public BaseResponse<String> register(@RequestBody AdminDetails adminDetails)
    {
        try {
            return adminService.sendCodeToMail(adminDetails.getEmail());
        }
        catch (Exception exception)
        {
            BaseResponse<String> baseResponse = new BaseResponse<>(exception.toString(), HttpStatus.INTERNAL_SERVER_ERROR.value(), false, exception.getMessage(), null);
            if (baseResponse.getError().contains("401")) {
                baseResponse.setCode(401);
            }
            return baseResponse;
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
