package com.example.authenticationService.repository;

import com.example.authenticationService.model.ResetPasswordRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ResetPasswordRepository extends CrudRepository<ResetPasswordRequest,Integer> {
    List<ResetPasswordRequest> findAll();
    Optional<ResetPasswordRequest> findByUsername(String userName);
}
