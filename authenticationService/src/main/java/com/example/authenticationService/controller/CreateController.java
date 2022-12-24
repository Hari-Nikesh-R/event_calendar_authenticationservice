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
    public BaseResponse<String> register(@RequestHeader(AUTHORIZATION) String token)
    {
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id=-1;
        try {
            id = restTemplate.exchange(AUTHENTICATION_URL + "/admin/fetch-id", HttpMethod.GET, entity, Integer.class).getBody();
        }
        catch (Exception exception)
        {
            id=-2;
            return adminService.sendCodeToMail(id);
        }
        if(Objects.nonNull(adminDetailsIntegerFetchInfoService.getInfoById(id)))
        {
            return adminService.sendCodeToMail(id);
        }
        return new BaseResponse<>(  "Not authorized User",HttpStatus.NOT_ACCEPTABLE.value(),false,"Admin has no rights to create",null);
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
