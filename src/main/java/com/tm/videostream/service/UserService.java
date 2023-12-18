package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tm.videostream.entity.Roles;
import com.tm.videostream.entity.User;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.response.JwtResponsePOJO;

public interface UserService {
	
	public ResponseEntity<String> saveRollDetails(Roles roles);
	
	public ResponseEntity<String> saveUserDetails(User user);
	
    public ResponseEntity<JwtResponsePOJO> generateTokens(SigninRequest signinRequest) throws CustomStreamException;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
}
