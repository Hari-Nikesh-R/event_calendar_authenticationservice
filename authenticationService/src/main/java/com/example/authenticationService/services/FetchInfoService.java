package com.example.authenticationService.services;

import com.example.authenticationService.dtos.UpdatePassword;

import java.util.List;

public interface FetchInfoService<T,K> {
    List<T> getAllInfo();
    K getId(String email);
    T getInfoById(Integer id);

    String changePassword(UpdatePassword updatePassword);
}
