package com.example.authenticationService.services;

import com.example.authenticationService.dtos.BaseResponse;

public interface StaffService {
    BaseResponse<String> resetPassword(Integer id);
    BaseResponse<String> verifyCode(Integer id,String code);
}
