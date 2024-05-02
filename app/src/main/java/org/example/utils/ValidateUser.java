package org.example.utils;

import org.example.models.UserInfoDTO;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidateUser {

  public void validate(UserInfoDTO userInfoDto){
    validateUserName(userInfoDto.getUsername());
    validatePassword(userInfoDto.getPassword());
    validateEmail(userInfoDto.getEmail());
  }

  static void validateUserName(String username){
    if(username == null || username.isEmpty()){
      throw new IllegalArgumentException("Username cannot be empty");
    }
  }

  static void validatePassword(String password){
    if(password == null || password.isEmpty()){
      throw new IllegalArgumentException("Password cannot be empty");
    }
    if(password.length() < 8){
      throw new IllegalArgumentException("Password must be at least 8 characters long");
    }
  }

  static void validateEmail(String email){
    if(email == null || email.isEmpty()){
      throw new IllegalArgumentException("Email cannot be empty");
    }
    if(!email.contains("@")){
      throw new IllegalArgumentException("Email must contain @");
    }

  }
}
