package com.cdac.onlineTiffinService.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cdac.onlineTiffinService.model.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
