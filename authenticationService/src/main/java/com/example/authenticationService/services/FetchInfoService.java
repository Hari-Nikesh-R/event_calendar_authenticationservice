package com.example.authenticationService.services;

import java.util.List;

public interface FetchInfoService<T,K> {
    List<T> getAllInfo();
    K getId(String email);
    T getInfoById(Integer id);
}
