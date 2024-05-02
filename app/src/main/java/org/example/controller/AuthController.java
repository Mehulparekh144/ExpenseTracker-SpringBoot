package org.example.controller;

import org.example.entities.RefreshToken;
import org.example.models.UserInfoDTO;
import org.example.response.JWTResponseDTO;
import org.example.service.JWTService;
import org.example.service.RefreshTokenService;
import org.example.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {

  @Autowired
  private JWTService jwtService;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @PostMapping("auth/v1/signup")
  public ResponseEntity Signup(@RequestBody UserInfoDTO userInfoDTO) {
    try {
      Boolean isSignedUp = userDetailsService.signupUser(userInfoDTO);
      if (Boolean.FALSE.equals(isSignedUp)) {
        return new ResponseEntity<>("Already exists", HttpStatus.BAD_REQUEST);
      }
      RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDTO.getUsername());
      String jwtToken = jwtService.generateToken(userInfoDTO.getUsername());
      return new ResponseEntity<>(JWTResponseDTO
              .builder()
              .accessToken(jwtToken)
              .token(refreshToken.getToken())
              .build(), HttpStatus.OK
      );
    } catch (Exception ex) {
      return new ResponseEntity<>( "Exception in User Service" , HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
