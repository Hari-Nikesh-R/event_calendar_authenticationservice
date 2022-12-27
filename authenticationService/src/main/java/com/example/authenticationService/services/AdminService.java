package com.example.authenticationService.services;

import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;

public interface AdminService {
    BaseResponse<String> sendCodeToMail(Integer id);
    BaseResponse<String> verifyCode(Integer id, String code, AdminDetails adminDetails);

}
