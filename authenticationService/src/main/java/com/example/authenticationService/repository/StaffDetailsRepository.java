package com.example.authenticationService.repository;

import com.example.authenticationService.model.StaffDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StaffDetailsRepository extends CrudRepository<StaffDetails,Integer> {
}
