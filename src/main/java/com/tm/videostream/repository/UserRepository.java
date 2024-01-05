package com.tm.videostream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.videostream.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	public User findByUsername(String username);

}
