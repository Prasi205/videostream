package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.RoleRequestPOJO;
import com.tm.videostream.pojo.UserRequestPOJO;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.response.JwtResponsePOJO;

public interface UserService {
	
	public String saveRollDetails(RoleRequestPOJO requestPojo);
	
	public String saveUserDetails(UserRequestPOJO userRequestPOJO);
	
    public ResponseEntity<JwtResponsePOJO> generateTokens(SigninRequest signinRequest) throws CustomStreamException;
	
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	public ResponseEntity<String> validateAccessToken(String username,String token);
	
}
