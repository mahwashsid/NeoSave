package com.example.Assignment.controller;

import com.example.Assignment.model.UserData;
import com.example.Assignment.service.UserService;
import org.apache.http.client.HttpResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import java.io.IOException;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping(value = "/create")
    public ResponseEntity createUser(@RequestBody UserData user) throws IOException, ServletException {
        try {
            ResponseEntity res = userService.saveUserData(user);
            return res;
        } catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(), HttpStatus.valueOf(e.getMessage()));
        }
    }



    @GetMapping(value = "/{id}")
    public ResponseEntity getUser(@PathVariable(value = "id") Long id) throws IllegalArgumentException {
        try{
            ResponseEntity res = userService.getUserData(id);
        return res;
    }
     catch (Exception e){
        return new ResponseEntity(e.getMessage(), HttpStatus.valueOf(e.getMessage()));
    }
    }


}
