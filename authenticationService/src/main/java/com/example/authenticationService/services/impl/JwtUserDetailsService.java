package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.AdminDetailsRepository;
import com.example.authenticationService.repository.StaffDetailsRepository;
import com.example.authenticationService.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.example.authenticationService.Utils.Configuration.AUTHORITIES_KEY;
import static com.example.authenticationService.Utils.Constants.*;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Autowired
    AdminDetailsRepository adminDetailsRepository;

    @Autowired
    StaffDetailsRepository staffDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(AUTHORITIES_KEY));
        if (DEFAULT_USER.equals(username)) {
            return new User(username, bCryptPasswordEncoder.encode(DEFAULT_PASSWORD), grantedAuthorityList);
        }
        switch (AUTHORITIES_KEY) {
            case STUDENT:
                Optional<StudentDetails> userCred = userDetailsRepository.findByEmail(username);
                if (userCred.isPresent()) {
                    if (userCred.get().getEmail().equals(username)) {
                        return new User(username, userCred.get().getPassword(), grantedAuthorityList);
                    }
                }
                break;
            case ADMIN:
                Optional<AdminDetails> adminCred = adminDetailsRepository.findByEmail(username);
                if (adminCred.isPresent()) {
                    if (adminCred.get().getEmail().equals(username)) {
                        return new User(username, adminCred.get().getPassword(), grantedAuthorityList);
                    }
                }
                break;
            case STAFF:
                Optional<StaffDetails> staffCred = staffDetailsRepository.findByEmail(username);
                if (staffCred.isPresent()) {
                    if (staffCred.get().getEmail().equals(username)) {
                        return new User(username, staffCred.get().getPassword(), grantedAuthorityList);
                    }

                }
                break;
            default:
                throw new UsernameNotFoundException(USER_NOT_FOUND + username);

        }
        throw new UsernameNotFoundException(USER_NOT_FOUND + username);
    }
}
