package com.example.Assignment.repository;

import com.example.Assignment.model.UserData;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserData, Long> {
    UserData findByEmail(String email);
}
