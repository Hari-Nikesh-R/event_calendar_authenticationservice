package com.example.authenticationService.repository;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffDetailsRepository extends CrudRepository<StaffDetails,Integer> {
    Optional<StaffDetails> findByEmail(String userName);
}
