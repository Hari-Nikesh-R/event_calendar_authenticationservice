package com.example.authenticationService.repository;

import com.example.authenticationService.model.AdminDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminDetailsRepository extends CrudRepository<AdminDetails,Integer> {
}
