package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.RoleRequestPOJO;
import com.tm.videostream.pojo.UserRequestPOJO;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.response.ResponsePOJO;

public interface UserService {

	public boolean saveRollDetails(RoleRequestPOJO requestPojo);

	public boolean saveSignUPDetails(UserRequestPOJO userRequestPOJO);

	public ResponseEntity<ResponsePOJO> generateToken(SigninRequest signinRequest) throws CustomStreamException;

	public ResponseEntity<String> validateToken(String accessToken, TokenValidationRequest tokenValidationRequest);

	public ResponseEntity<ResponsePOJO> regenerateTokens(String refreshToken,
			TokenValidationRequest tokenValidationRequest);

}
