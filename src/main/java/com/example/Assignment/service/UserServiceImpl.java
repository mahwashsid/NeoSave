package com.example.Assignment.service;


import com.example.Assignment.model.UserData;
import com.example.Assignment.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.regex.Pattern;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserRepository userRepository;

    @Value("${PINCODE_STATE_URI}")
    private String uri;


    public ResponseEntity saveUserData(UserData user){
      try {
    if(!validateFullName(user.getName())){
        return new ResponseEntity("Please enter full name", HttpStatus.BAD_REQUEST);
    }
    else if (!validatePincode(user.getAddressPincode())){
        return new ResponseEntity("Invalid Pincode", HttpStatus.BAD_REQUEST);
    }
    else if (!validateEmail(user.getEmail())){
        return new ResponseEntity("Invalid Email", HttpStatus.BAD_REQUEST);
    }
    else if(userRepository.findByEmail(user.getEmail())!=null){
        return new ResponseEntity("Email Already Present", HttpStatus.BAD_REQUEST);
    }
          UserData saveUser = UserData.builder()
                  .name(user.getName())
                  .email(user.getEmail())
                  .addressPincode(user.getAddressPincode())
                  .stateName(getStateFromPincode(user.getAddressPincode()))
                  .build();
          userRepository.save(saveUser);
          return new ResponseEntity(saveUser.getId(), HttpStatus.CREATED);
      }
        catch(Exception e){
          return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    private boolean validateFullName(String name){
        String regexName = "\\p{Upper}(\\p{Lower}+\\s?)";
        String patternName = "(" + regexName + "){2,3}";
        boolean b = name.matches(patternName);
        return b;
    }


    private boolean validatePincode(String pincode)
    {
        return pincode.matches( "^[1-9][0-9]{5}$");
    }

    private boolean validateEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private String getStateFromPincode(String pincode) throws JSONException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        String url = uri+pincode;
       ResponseEntity res =  restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONArray obj = new JSONArray((String) res.getBody());
        String stateName = obj.getJSONObject(0).getJSONArray("PostOffice").getJSONObject(0).getString("State");
        return stateName;
    }



    public ResponseEntity getUserData(Long id){
    try {
        if (!userRepository.findById(id).isEmpty()) {
            System.out.println("Line 2");
            return new ResponseEntity(userRepository.findById(id), HttpStatus.OK);
        } else
            return new ResponseEntity("Not Found", HttpStatus.BAD_REQUEST);
    }
    catch (Exception e){
        return new ResponseEntity(e.getMessage(), HttpStatus.valueOf(e.getMessage()));
    }
    }



}
