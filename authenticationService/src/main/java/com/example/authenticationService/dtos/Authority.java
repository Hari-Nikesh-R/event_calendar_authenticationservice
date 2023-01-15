package com.example.authenticationService.dtos;

import lombok.Data;

@Data
public class Authority
{
    private String email;
    private boolean isAuthorized;
}
