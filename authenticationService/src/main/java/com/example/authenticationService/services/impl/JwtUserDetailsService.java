package com.example.authenticationService.services.impl;

import com.example.authenticationService.dtos.AuthDto;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.UserDetailsInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserDetailsInformation userDetailsInformation;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<StudentDetails> userCred = userDetailsInformation.findByEmail(username);
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if(username.equals(userCred.get().getEmail()) && userCred.get().getRole().equals("STUDENT")) {
            AuthDto authDto = new AuthDto(username,userCred.get().getPassword(),new ArrayList<>());
            authDto.setRole(userCred.get().getRole());
            return authDto;
        }
         else {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
    }
}
