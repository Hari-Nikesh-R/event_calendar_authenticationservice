package com.example.authenticationService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse<T> {
    private String result;
    private int code;
    private Boolean success;
    private String error;
    private T value;
}
