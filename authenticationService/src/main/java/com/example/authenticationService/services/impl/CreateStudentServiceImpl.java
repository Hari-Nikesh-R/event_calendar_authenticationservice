package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.UserDetailsRepository;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateStudentServiceImpl implements RegisterService<StudentDetails> {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public StudentDetails save(StudentDetails studentDetails) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(studentDetails.getPassword());
        studentDetails.setPassword(password);
       return userDetailsRepository.save(studentDetails);
    }
}
