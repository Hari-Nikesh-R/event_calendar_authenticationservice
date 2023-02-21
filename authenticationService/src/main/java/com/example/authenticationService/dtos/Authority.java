package com.example.authenticationService.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authority
{
    private String email;
    private boolean isAuthorized;
}
