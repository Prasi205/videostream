package com.tm.videostream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.videostream.entity.Roles;

public interface RoleRepository extends JpaRepository<Roles, Integer> {

}
