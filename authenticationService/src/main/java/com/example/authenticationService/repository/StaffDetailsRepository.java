package com.example.authenticationService.repository;

import com.example.authenticationService.model.StaffDetails;
import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StaffDetailsRepository extends CrudRepository<StaffDetails,Integer> {
    Optional<StaffDetails> findByEmail(String userName);
    List<StaffDetails> findAll();
    Optional<StaffDetails> findById(Integer id);
    @Query(value = "select id from staff_detail where email = ?1",nativeQuery = true)
    Optional<Integer> fetchId(String email);
}
