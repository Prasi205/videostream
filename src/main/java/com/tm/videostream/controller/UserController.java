package com.tm.videostream.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.videostream.entity.Roles;
import com.tm.videostream.entity.User;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.response.JwtResponsePOJO;
import com.tm.videostream.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*")
@RequestMapping("/user")
public class UserController {
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
			
	@Autowired
	private UserService userService;
	
	@PostMapping("/saveRollDetails")
	public ResponseEntity<String> saveRollDetails(@RequestBody Roles roleDetails){
		logger.info("Received the request to save the roll details");
		ResponseEntity<String> saveRollDetails;
		try {
			logger.info("Roll details saving requested is received");
			saveRollDetails=userService.saveRollDetails(roleDetails);
		} catch (Exception e) {
			logger.error("Unable to received roll saving request");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to received roll saving request");
		}
		return saveRollDetails;
	}
	
	/**Handles the user details saving process based on the received request
	 * @param user
	 * @return User
	 */
	@PostMapping("/saveuser")
	public ResponseEntity<String> saveUserDetails(@RequestBody User user) {
		logger.info("Received request to save the user details");
		ResponseEntity<String> saveUserDetails;;
		try {
			logger.info("User details are saved successfully");
			saveUserDetails=userService.saveUserDetails(user);
		} catch (Exception e) {
			logger.error("Unable to received the request");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to received user details saving request");
		}
		return saveUserDetails;
	}
		
	/**Handles the generation of access and refresh tokens based on the received request
	 * @param signinRequest
	 * @return StreamResponsePOJO
	 */
	@PostMapping("/")
	public ResponseEntity<JwtResponsePOJO> generateTokens(@RequestBody SigninRequest signinRequest) {
		logger.info("Received request to generate token");
		ResponseEntity<JwtResponsePOJO> jwtresponsePojo;
		try {
			logger.info("Token generation request received successfully");
			jwtresponsePojo=userService.generateTokens(signinRequest);
		} catch (Exception e) {
			logger.error("Unable to received the token details");
			throw new CustomStreamException("Unable to received the token details");
		}
		return jwtresponsePojo;
	}
	
}
