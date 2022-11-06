package com.example.authenticationService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private String result;
    private String code;
    private Boolean success;
    private String error;
    private T value;
}
