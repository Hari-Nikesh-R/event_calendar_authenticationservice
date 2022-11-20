package com.example.authenticationService.repository;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminDetailsRepository extends CrudRepository<AdminDetails,Integer> {
    Optional<AdminDetails> findByEmail(String userName);
}
