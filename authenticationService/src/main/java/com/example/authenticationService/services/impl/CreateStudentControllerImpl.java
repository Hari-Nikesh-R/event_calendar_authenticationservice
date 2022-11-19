package com.example.authenticationService.services.impl;

import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.UserDetailsInformation;
import com.example.authenticationService.services.CreateStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateStudentControllerImpl implements CreateStudentService {

    @Autowired
    UserDetailsInformation userDetailsInformation;

    @Override
    public StudentDetails save(StudentDetails studentDetails) {
       return userDetailsInformation.save(studentDetails);
    }
}
