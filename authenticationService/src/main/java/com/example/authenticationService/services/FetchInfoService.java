package com.example.authenticationService.services;

import com.example.authenticationService.dtos.UpdatePassword;

import java.util.List;

public interface FetchInfoService<T,K> {
    List<T> getAllInfo();
    K getId(String username);
    T getInfoById(Integer id);

    T getInfoByEmail(String email);

    String changePassword(UpdatePassword updatePassword);
    String forgotPasswordReset(UpdatePassword updatePassword);
    T updateProfile(T details,String email);
    Boolean validateByEmail(String email);
}
