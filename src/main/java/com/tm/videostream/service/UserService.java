package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.request.RoleRequestPOJO;
import com.tm.videostream.pojo.request.SigninRequestPOJO;
import com.tm.videostream.pojo.request.TokenRegenerationRequestPOJO;
import com.tm.videostream.pojo.request.UserRequestPOJO;
import com.tm.videostream.response.ResponsePOJO;

public interface UserService {

	public boolean saveRollDetails(RoleRequestPOJO requestPojo);

	public boolean saveSignUPDetails(UserRequestPOJO userRequestPOJO);

	public ResponseEntity<ResponsePOJO> generateToken(SigninRequestPOJO signinRequest) throws CustomStreamException;

	public ResponseEntity<String> validateToken(String accessToken);

	public ResponseEntity<ResponsePOJO> regenerateTokens(String refreshToken,
			        TokenRegenerationRequestPOJO tokenRegenerationRequest);

}
