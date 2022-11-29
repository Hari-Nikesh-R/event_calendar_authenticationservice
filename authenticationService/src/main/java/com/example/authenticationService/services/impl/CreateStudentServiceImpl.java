package com.example.authenticationService.services.impl;

import com.example.authenticationService.controller.StudentController;
import com.example.authenticationService.dtos.UpdatePassword;
import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import com.example.authenticationService.repository.UserDetailsRepository;
import com.example.authenticationService.services.FetchInfoService;
import com.example.authenticationService.services.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class CreateStudentServiceImpl implements RegisterService<StudentDetails>, FetchInfoService<StudentDetails,Integer> {

    @Autowired
    UserDetailsRepository userDetailsRepository;

    @Override
    public StudentDetails save(StudentDetails studentDetails) {
        Optional<StudentDetails> optionalStudentDetails = userDetailsRepository.findByUsername(studentDetails.getUsername());
        if(optionalStudentDetails.isPresent())
        {
            return null;
        }
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        String password = bCryptPasswordEncoder.encode(studentDetails.getPassword());
        studentDetails
                .setPassword(password);
       return userDetailsRepository.save(studentDetails);
    }

    @Override
    public List<StudentDetails> getAllInfo() {
        List<StudentDetails> studentDetails = userDetailsRepository.findAll();
        for(StudentDetails studentDetail : studentDetails)
        {
            studentDetail.setPassword("");
        }
        return studentDetails;
    }

    @Override
    public Integer getId(String userName) {
        Optional<Integer> optionalStudentId = userDetailsRepository.fetchId(userName);
        return optionalStudentId.orElse(null);
    }

    @Override
    public StudentDetails getInfoById(Integer id) {
        Optional<StudentDetails>  optionalStudentDetails = userDetailsRepository.findById(id);
        return optionalStudentDetails.orElse(null);

    }

    @Override
    public String changePassword(UpdatePassword updatePassword,Boolean isResetPassword) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        StudentDetails studentDetails = getInfoById(updatePassword.getId());

        if(Objects.nonNull(studentDetails)) {
            if(isResetPassword){
                studentDetails.setPassword(bCryptPasswordEncoder.encode("Student@123"));
                userDetailsRepository.save(studentDetails);
                return "Password Reset Successful";
            }
            else {
                studentDetails.setPassword(bCryptPasswordEncoder.encode(updatePassword.getPassword()));
                userDetailsRepository.save(studentDetails);
                return "Password Updated Successfully";
            }
        }
        return "Failed to Update password";
    }
}
