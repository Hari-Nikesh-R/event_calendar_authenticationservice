package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.UserDetailsRepository;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateStudentServiceImpl implements RegisterService<StudentDetails> {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public StudentDetails save(StudentDetails studentDetails) {
       return userDetailsRepository.save(studentDetails);
    }
}
