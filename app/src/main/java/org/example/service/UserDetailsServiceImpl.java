package org.example.service;

import org.example.entities.UserInfo;
import org.example.models.UserInfoDTO;
import org.example.repository.UserRepository;
import org.example.utils.ValidateUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    UserInfo userInfo = userRepository.findByUsername(username);
    if(userInfo == null) {
      throw new UsernameNotFoundException("User not found with username: " + username);
    }
    return new CustomUserDetails(userInfo);
  }

  public UserInfo checkIfUserExists(UserInfoDTO userInfoDto){
    return userRepository.findByUsername(userInfoDto.getUsername());
  }

  public Boolean signupUser(UserInfoDTO userInfoDto){
    ValidateUser.validate(userInfoDto);
    userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
    if(Objects.nonNull(checkIfUserExists(userInfoDto))){
      return false;
    }
    String userId = UUID.randomUUID().toString();
    userRepository.save(new UserInfo(userId, userInfoDto.getUsername(), userInfoDto.getPassword()
            , new HashSet<>()
            ));
    return true;
  }
}
