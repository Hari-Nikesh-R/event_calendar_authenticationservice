package com.example.authenticationService.controller;

import com.example.authenticationService.Utils.Constants;
import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.EmailDetails;
import com.example.authenticationService.dtos.UpdatePassword;
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
@RequestMapping(ADMIN_URL)
public class AdminController {

    @Autowired
    FetchInfoService<AdminDetails,Integer> adminDetailsIntegerFetchInfoService;
    @Autowired
    AdminService adminService;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping(value = "/info")
    public BaseResponse<AdminDetails> getAdminDetail(@RequestHeader(AUTHORIZATION) String token){
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/admin/fetch-id", HttpMethod.GET,entity,Integer.class).getBody();
        AdminDetails adminDetails = adminDetailsIntegerFetchInfoService.getInfoById(id);
        if(Objects.nonNull(adminDetails))
        {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true,"",adminDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(), HttpStatus.NO_CONTENT.value(), false,"No Admin User Found",null);
    }
    @PostMapping(value = "/validate/email")
    public Boolean isAdminAvailable(@RequestBody EmailDetails emailDetails)
    {
        return adminDetailsIntegerFetchInfoService.validateByEmail(emailDetails.getRecipient());
    }

    @GetMapping(value = FETCH_ID)
    public synchronized Integer getAdminId(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        String email =jwtTokenUtil.getUsernameFromToken(token);
        Integer adminInfoId = adminDetailsIntegerFetchInfoService.getId(email);
        if(Objects.nonNull(adminInfoId)) {
           return adminInfoId;
        }
        return -1;
    }
    @PutMapping(value = "/update/password")
        public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword, @RequestHeader(AUTHORIZATION) String token){
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/admin/fetch-id", HttpMethod.GET,entity,Integer.class).getBody();
        updatePassword.setId(id);
        String isUpdated = adminDetailsIntegerFetchInfoService.changePassword(updatePassword);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
    }

    @PutMapping(value = "/change/password")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword){

        String isUpdated = adminDetailsIntegerFetchInfoService.forgotPasswordReset(updatePassword);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
    }

    @PutMapping(value = "/update/profile")
    public BaseResponse<AdminDetails> updateAdminDetails(@RequestBody AdminDetails adminDetails, @RequestHeader(AUTHORIZATION) String token)
    {
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/admin/fetch-id",HttpMethod.GET,entity,Integer.class).getBody();
        AdminDetails updatedDetail = adminDetailsIntegerFetchInfoService.updateProfile(adminDetails,id);
        if(Objects.nonNull(updatedDetail))
        {
            return new BaseResponse<>("Update Successful",HttpStatus.OK.value(),true,"",updatedDetail);
        }
        return new BaseResponse<>("Not Updated",HttpStatus.NON_AUTHORITATIVE_INFORMATION.value(), false,"updateDetails is null",null);


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
