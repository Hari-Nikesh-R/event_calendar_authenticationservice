package com.example.authenticationService.repository;

import com.example.authenticationService.model.AdminDetails;
import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdminDetailsRepository extends CrudRepository<AdminDetails,Integer> {
    Optional<AdminDetails> findByEmail(String userName);
    List<AdminDetails> findAll();
    Optional<AdminDetails> findById(Integer id);
    @Query(value = "select id from admin_details where email = ?1",nativeQuery = true)
    Optional<Integer> fetchId(String email);


}
