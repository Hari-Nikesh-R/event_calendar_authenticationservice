package com.example.authenticationService.services;

import com.example.authenticationService.model.StudentDetails;

public interface CreateStudentService {
    StudentDetails save(StudentDetails studentDetails);
}
