package com.tm.videostream.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.request.RoleRequestPOJO;
import com.tm.videostream.pojo.request.SigninRequestPOJO;
import com.tm.videostream.pojo.request.TokenRegenerationRequestPOJO;
import com.tm.videostream.pojo.request.UserRequestPOJO;
import com.tm.videostream.response.ResponsePOJO;
import com.tm.videostream.service.UserService;

/**
 * This controller is used to save rolls,users,new token generations and
 * regenerate tokens
 */
@RestController
@CrossOrigin(origins = "http://localhost:3001", allowedHeaders = "*")
@RequestMapping("/auth")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	/**
	 * Handles the roll details saving process based on received request
	 * 
	 * @param roleRequestPojo
	 * @return ResponsePOJO
	 */
	@PostMapping("/saveRollDetails")
	public ResponsePOJO saveRollDetails(@RequestBody @Valid RoleRequestPOJO roleRequestPojo) {
		logger.info("Received the request to save the roll details");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Role details saving request is received");
			if (userService.saveRollDetails(roleRequestPojo)) {
				logger.info("Role details are saved in database");
				responsePOJO.response("Role details are saved in database", null, true);
			} else {
				logger.error("Unable to save role details");
				responsePOJO.response("Unable to save the role details", null, false);
			}
		} catch (Exception e) {
			logger.error("Unable to received roll saving request");
			throw new CustomStreamException("Unable to received roll saving request");
		}
		return responsePOJO;
	}

	/**
	 * Handles the user details saving process based on the received request
	 * 
	 * @param userRequestPOJO
	 * @return ResponsePOJO
	 */
	@PostMapping("/signup")
	public ResponsePOJO saveSignUPDetails(@RequestBody @Valid UserRequestPOJO userRequestPOJO) {
		logger.info("Received request to save the user details");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("User details saving request is received");
			if (userService.saveSignUPDetails(userRequestPOJO)) {
				logger.info("User details are saved in database");
				responsePOJO.response("User details are saved in database", null, true);
			} else {
				logger.error("Given username is already exist");
				responsePOJO.response("Given username is already exist", null, false);
			}
		} catch (Exception e) {
			logger.error("Unable to received the user saving request");
			throw new CustomStreamException("Unable to received the user saving request");
		}
		return responsePOJO;
	}

	/**
	 * Handles the generation of access and refresh tokens based on the received
	 * request
	 * 
	 * @param signinRequest
	 * @return ResponseEntity<ResponsePOJO>
	 */
	@PostMapping("/signin")
	public ResponseEntity<ResponsePOJO> signin(@RequestBody @Valid SigninRequestPOJO signinRequest) {
		logger.info("Received request to generate token");
		ResponseEntity<ResponsePOJO> responsePojo;
		try {
			logger.info("Token generation request received successfully");
			responsePojo = userService.generateToken(signinRequest);
		} catch (Exception e) {
			logger.error("Unable to received the token details request");
			throw new CustomStreamException("Unable to generate the tokens");
		}
		return responsePojo;
	}

	/**
	 * Handles the regeneration of access and refresh tokens based on the received
	 * request
	 * 
	 * @param refreshToken
	 * @param tokenValidationRequest
	 * @return ResponseEntity<ResponsePOJO>
	 */
	@PostMapping("/regenerateTokens")
	public ResponseEntity<ResponsePOJO> regenerateTokens(@RequestBody @Valid TokenRegenerationRequestPOJO tokenValidationRequest,
			@RequestHeader(value = "Authorization", defaultValue = "") String refreshToken) {
		logger.info("Received the request to regenerate the Tokens");
		ResponseEntity<ResponsePOJO> responsePOJO;
		try {
			logger.info("Token generation request received successfully");
			responsePOJO = userService.regenerateTokens(refreshToken, tokenValidationRequest);
		} catch (Exception e) {
			logger.error("Unable to received the regenerate tokens request");
			throw new CustomStreamException("Unable to regenerate the tokens");
		}
		return responsePOJO;
	}

}
