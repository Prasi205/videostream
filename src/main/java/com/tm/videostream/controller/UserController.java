package com.tm.videostream.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.RoleRequestPOJO;
import com.tm.videostream.pojo.UserRequestPOJO;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.response.JwtResponsePOJO;
import com.tm.videostream.service.UserService;

/**This controller is used to save rolls,users,new token generations and regenerate tokens*/
@RestController
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
			
	@Autowired
	private UserService userService;
	
	/**Handles the roll details saving process based on received request
	 * @param roleRequestPojo
	 * @return String
	 */
	@PostMapping("/saveRollDetails")
	public ResponseEntity<String> saveRollDetails(@RequestBody @Valid RoleRequestPOJO roleRequestPojo){
		logger.info("Received the request to save the roll details");
		try {
			String saveRollDetails=userService.saveRollDetails(roleRequestPojo);
			logger.info("Roll details saving requested is received");
			return ResponseEntity.ok(saveRollDetails);
		} catch (Exception e) {
			logger.error("Unable to received roll saving request");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to received roll saving request");
		}
	}
	
	/**Handles the user details saving process based on the received request
	 * @param userRequestPOJO
	 * @return String
	 */
	@PostMapping("/signup")
	public ResponseEntity<String> saveSignUPDetails(@RequestBody @Valid UserRequestPOJO userRequestPOJO) {
		logger.info("Received request to save the user details");
		try {
			String saveUserDetails=userService.saveSignUPDetails(userRequestPOJO);
			logger.info("User details are saved successfully");
			return ResponseEntity.ok(saveUserDetails);
		} catch (Exception e) {
			logger.error("Unable to received the request");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to received user details saving request");
		}
	}
		
	/**Handles the generation of access and refresh tokens based on the received request
	 * @param signinRequest
	 * @return JwtResponsePOJO
	 */
	@PostMapping("/signin")
	public ResponseEntity<JwtResponsePOJO> generateTokens(@RequestBody SigninRequest signinRequest) {
		logger.info("Received request to generate token");
		ResponseEntity<JwtResponsePOJO> jwtresponsePojo;
		try {
			logger.info("Token generation request received successfully");
			jwtresponsePojo=userService.generateTokens(signinRequest);
		} catch (Exception e) {
			logger.error("Unable to received the token details request");
			throw new CustomStreamException("Unable to received the token details request");
		}
		return jwtresponsePojo;
	}
	
	/**This method is used to regenerate the tokens
	 * @param refreshToken
	 * @param tokenValidationRequest
	 * @return JwtResponsePOJO
	 */
	@PostMapping("/regenerateTokens")
	public ResponseEntity<JwtResponsePOJO> regenerateTokens(@RequestBody TokenValidationRequest tokenValidationRequest,
			@RequestHeader(value = "Authorization", defaultValue = "") String refreshToken ){
		logger.info("Received the request to regenerate the Tokens");
		ResponseEntity<JwtResponsePOJO> jwtresponsePojo;
		try {
			logger.info("Token generation request received successfully");
			jwtresponsePojo=userService.regenerateTokens(refreshToken, tokenValidationRequest);
		} catch (Exception e) {
			logger.error("Unable to received the regenerate tokens request");
			throw new CustomStreamException("Unable to received the regenerate tokens request");
		}
		return jwtresponsePojo;
	}
	
}
