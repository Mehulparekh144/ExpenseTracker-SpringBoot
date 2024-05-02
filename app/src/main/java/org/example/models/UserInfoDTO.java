package org.example.models;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.example.entities.UserInfo;

import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class UserInfoDTO extends UserInfo {
  private String username;
  private String lastname;
  private Long phoneNumber;
  private String email;

}
