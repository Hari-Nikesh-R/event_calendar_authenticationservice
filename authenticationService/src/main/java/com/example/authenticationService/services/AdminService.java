package com.example.authenticationService.services;

import com.example.authenticationService.dtos.Authority;
import com.example.authenticationService.dtos.BaseResponse;
import com.example.authenticationService.model.AdminDetails;

public interface AdminService {
    BaseResponse<String> sendCodeToMail(String email);
    BaseResponse<String> verifyCode(String code, AdminDetails adminDetails);
    String updateAuthority(Authority authority);
    Boolean isAuthorizedUser(String email);

}
