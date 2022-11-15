package com.example.authenticationService.controller;

import com.example.authenticationService.model.AuthDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "login/")
public class AuthController {
    @PostMapping
    public void loginUser(AuthDto authDto)
    {

    }
}
