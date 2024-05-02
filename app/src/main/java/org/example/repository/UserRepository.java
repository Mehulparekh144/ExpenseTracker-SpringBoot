package org.example.repository;

import org.example.entities.UserInfo;
import org.springframework.data.repository.CrudRepository;


// For CrudRepository<T,R> T is the entity type and R is the type of the primary key
public interface UserRepository extends CrudRepository<UserInfo , Long> {
  UserInfo findByUsername(String username);
}
