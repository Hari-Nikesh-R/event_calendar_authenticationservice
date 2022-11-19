package com.example.authenticationService.repository;

import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailsInformation extends CrudRepository<StudentDetails, String> {

}
