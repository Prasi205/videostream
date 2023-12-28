package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.RoleRequestPOJO;
import com.tm.videostream.pojo.UserRequestPOJO;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.response.JwtResponsePOJO;

public interface UserService {
	
	public String saveRollDetails(RoleRequestPOJO requestPojo);
	
	public String saveSignUPDetails(UserRequestPOJO userRequestPOJO);
	
    public ResponseEntity<JwtResponsePOJO> generateTokens(SigninRequest signinRequest) throws CustomStreamException;
	
    public ResponseEntity<String> validateToken(String accessToken, TokenValidationRequest tokenValidationRequest);
	
    public ResponseEntity<JwtResponsePOJO> regenerateTokens(String refreshToken,TokenValidationRequest tokenValidationRequest);
	
}
