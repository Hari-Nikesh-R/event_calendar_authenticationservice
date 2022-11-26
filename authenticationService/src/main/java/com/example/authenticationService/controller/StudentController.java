package com.example.authenticationService.controller;

import com.example.authenticationService.config.JwtTokenUtil;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.services.FetchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Objects;

import static com.example.authenticationService.Utils.Constants.*;
import static com.example.authenticationService.Utils.Urls.AUTHENTICATION_URL;

@RestController
@CrossOrigin("*")
@RequestMapping(value = "/student")

public class StudentController {
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    FetchInfoService<StudentDetails,Integer> studentDetailsFetchInfoService;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @GetMapping(value = "/info/{id}")
    public BaseResponse<StudentDetails> getStudentDetail(@PathVariable("id") Integer id){
        StudentDetails studentDetails = studentDetailsFetchInfoService.getInfoById(id);
        if(Objects.nonNull(studentDetails))
        {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true,"",studentDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(), HttpStatus.NO_CONTENT.value(), false,"No Student Found",null);
    }

    @GetMapping(value = "/fetch-id")
    public Integer getStudentId(@RequestHeader("Authorization") String token){
        token = token.replace("Bearer ","");
        String email =jwtTokenUtil.getUsernameFromToken(token);
        Integer studentInfoId = studentDetailsFetchInfoService.getId(email);
        if(Objects.nonNull(studentInfoId)) {
            return studentInfoId;
        }
        return -1;
    }

    @GetMapping(value = "/student-info/all")
    @PreAuthorize(ADMIN_ACCESS+" or "+STAFF_ACCESS)
    public BaseResponse<List<StudentDetails>> getAllStudents(){
        List<StudentDetails> studentDetails = studentDetailsFetchInfoService.getAllInfo();
        if(Objects.nonNull(studentDetails)) {
            return new BaseResponse<>(HttpStatus.ACCEPTED.toString(), HttpStatus.OK.value(), true, "", studentDetails);
        }
        return new BaseResponse<>(HttpStatus.NO_CONTENT.toString(),HttpStatus.NO_CONTENT.value(),true,"No Students found",null);
    }
    @PutMapping(value = "/update/password")
    public BaseResponse<String> updatePassword(@RequestBody UpdatePassword updatePassword, @RequestHeader("Authorization") String token){
        HttpEntity<String> entity = setTokenInHeaders(token);
        Integer id = restTemplate.exchange(AUTHENTICATION_URL + "/student/fetch-id", HttpMethod.GET,entity,Integer.class).getBody();
        updatePassword.setId(id);
        String isUpdated = studentDetailsFetchInfoService.changePassword(updatePassword);
        if(Objects.nonNull(isUpdated))
        {
            return new BaseResponse<>("Updated", HttpStatus.OK.value(), true,"",isUpdated);
        }
        return new BaseResponse<>(HttpStatus.UPGRADE_REQUIRED.toString(), HttpStatus.UPGRADE_REQUIRED.value(), false,"Failed to update password",null);
    }
    private HttpEntity<String> setTokenInHeaders(String token){
        HttpHeaders httpHeaders = getHeaders();
        httpHeaders.set("Authorization",token);
        return new HttpEntity<>(httpHeaders);
    }
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }

}
