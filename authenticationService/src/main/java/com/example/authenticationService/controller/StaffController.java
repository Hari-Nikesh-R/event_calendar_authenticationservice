package com.example.authenticationService.controller;

import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.*;


@RestController
@CrossOrigin("*")
@RequestMapping(value = STAFF_URL)
public class StaffController {

    @Autowired
    FetchInfoService<StaffDetails,Integer> staffDetailsIntegerFetchInfoService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    RestTemplate restTemplate;


    @GetMapping(value = "/info/{id}")
    @PreAuthorize(ADMIN_ACCESS+" or "+STAFF_ACCESS)
    public BaseResponse<StaffDetails> getStaffDetail(@PathVariable("id") Integer id){
        StaffDetails staffDetails = staffDetailsIntegerFetchInfoService.getInfoById(id);
        if(Objects.nonNull(staffDetails))
        {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true,"",staffDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(), HttpStatus.NO_CONTENT.value(), false,"No Staff Found",null);
    }

    @GetMapping(value = FETCH_ID)
    @PreAuthorize(ADMIN_ACCESS+" or "+STAFF_ACCESS)
    public Integer getStaffId(@RequestHeader(AUTHORIZATION) String token){
        token = token.replace("Bearer ","");
        String email =jwtTokenUtil.getUsernameFromToken(token);
        Integer staffInfoId = staffDetailsIntegerFetchInfoService.getId(email);
        if(Objects.nonNull(staffInfoId)) {
            return staffInfoId;
        }
        return -1;
    }
    @GetMapping(value = "/staff-info/all")
    public BaseResponse<List<StaffDetails>> getAllStaff(){
        List<StaffDetails> staffDetails = staffDetailsIntegerFetchInfoService.getAllInfo();
        if(Objects.nonNull(staffDetails)) {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true, "", staffDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(),HttpStatus.NO_CONTENT.value(),true,"No Staff found",null);
    }

    @PutMapping(value = "/update/password")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword, @RequestHeader(AUTHORIZATION) String token){
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/staff/fetch-id", HttpMethod.GET,entity,Integer.class).getBody();
        updatePassword.setId(id);
        String isUpdated = staffDetailsIntegerFetchInfoService.changePassword(updatePassword,false);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
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
