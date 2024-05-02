package org.example.controller;

import org.example.entities.RefreshToken;
import org.example.request.AuthRequestDTO;
import org.example.request.RefreshTokenRequestDTO;
import org.example.response.JWTResponseDTO;
import org.example.service.JWTService;
import org.example.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class TokenController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private RefreshTokenService refreshTokenService;

  @Autowired
  private JWTService jwtService;


  @PostMapping("auth/v1/login")
  public ResponseEntity Login(@RequestBody AuthRequestDTO authRequestDTO){
    Authentication authentication =
            authenticationManager.authenticate(new
                    UsernamePasswordAuthenticationToken(authRequestDTO.getUsername()
                    , authRequestDTO.getPassword()));
    if(authentication.isAuthenticated()){
      RefreshToken refreshToken =
              refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
      return new ResponseEntity<>(JWTResponseDTO
              .builder()
              .accessToken(jwtService.generateToken(authRequestDTO.getUsername()))
              .token(refreshToken.getToken())
              .build() , HttpStatus.OK);
    } else{
      return new ResponseEntity<>("Exception for User Service" ,
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("auth/v1/refreshToken")
  public JWTResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
    return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
            .map(refreshTokenService::verifyExpiration)
            .map(RefreshToken::getUserInfo)
            .map(userInfo -> {
              String accessToken = jwtService.generateToken(userInfo.getUsername());
              return JWTResponseDTO.builder().accessToken(accessToken).token(refreshTokenRequestDTO.getToken()).build();
            }).orElseThrow(()-> new RuntimeException("Refresh token not in DB..."));
  }
}
