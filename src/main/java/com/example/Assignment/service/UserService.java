package com.example.Assignment.service;

import com.example.Assignment.model.UserData;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity saveUserData(UserData user);
    ResponseEntity getUserData(Long id);
}
