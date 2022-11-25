package com.example.authenticationService.repository;

import com.example.authenticationService.model.StudentDetails;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Repository
public interface UserDetailsRepository extends CrudRepository<StudentDetails, String> {
    Optional<StudentDetails> findByEmail(String userName);
    List<StudentDetails> findAll();

    Optional<StudentDetails> findById(Integer id);

    @Query(value = "select id from student_details where email = ?1",nativeQuery = true)
    Optional<Integer> fetchId(String email);


}
