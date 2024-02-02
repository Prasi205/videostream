package com.tm.videostream.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tm.videostream.entity.Roles;
import com.tm.videostream.entity.User;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.TokenPropertiesPOJO;
import com.tm.videostream.pojo.request.RoleRequestPOJO;
import com.tm.videostream.pojo.request.SigninRequestPOJO;
import com.tm.videostream.pojo.request.TokenRegenerationRequestPOJO;
import com.tm.videostream.pojo.request.UserRequestPOJO;
import com.tm.videostream.repository.RoleRepository;
import com.tm.videostream.repository.UserRepository;
import com.tm.videostream.response.ResponsePOJO;
import com.tm.videostream.service.UserService;

/**
 * This class provides the implementation of the UserService interface. It
 * contains methods to handles save roll details,userdetails, generate new
 * tokens,validate the token and regenerate the tokens.
 */
@Service
public class UserServiceImpl implements UserService {

	Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${jwt.secret}")
	private String secretKey;

	@Value("${jwt.accessTokenValidity}")
	private int accessTokenValidity;

	@Value("${jwt.refreshTokenValidity}")
	private int refreshTokenValidity;

	/**
	 * This method is used to save the roll details in database
	 * 
	 * @param roleRequestPojo
	 * @return boolean
	 */
	public boolean saveRollDetails(RoleRequestPOJO roleRequestPojo) {
		logger.info("Received request to save the role details");
		try {
			Roles saveRollDetails = new Roles();
			saveRollDetails.setRoleName("ROLE_" + roleRequestPojo.getRoleName());
			roleRepository.save(saveRollDetails);
			logger.info("Roles details are added in database");
			return true;
		} catch (Exception e) {
			logger.error("Unable to save roll details in database");
			return false;
		}
	}

	/**
	 * This method is used to save the user details in database
	 * 
	 * @param userRequestPOJO
	 * @return boolean
	 */
	public boolean saveSignUPDetails(UserRequestPOJO userRequestPOJO) {
		logger.info("Received request to save the user details in database");
		try {
			User user = new User();

			user.setName(userRequestPOJO.getName());
			user.setEmail(userRequestPOJO.getEmail());
			user.setUsername(userRequestPOJO.getUsername());

			/* Password convert as Bcrypt format */
			user.setPassword(passwordEncoder.encode(userRequestPOJO.getPassword()));

			String uuid = UUID.randomUUID().toString();
			user.setUniqueId(uuid);
			
			Roles roles = roleRepository.findByRoleName("ROLE_" + userRequestPOJO.getRoleName());

			user.setRoles(roles);
            
			if (userRepository.existsByUsername(userRequestPOJO.getUsername())) {
				logger.info("Given username is already exist");
				return false;
			}else {
				userRepository.save(user);
				logger.info("User details saved in database");
				return true;
			}
		} catch (Exception e) {
			logger.error("Unable to save the user details");
			return false;
		}
	}

	/**
	 * This method is used to generate the tokens and return in response
	 * 
	 * @param signinRequest
	 * @return ResponseEntity<ResponsePOJO>
	 */
	public ResponseEntity<ResponsePOJO> generateToken(SigninRequestPOJO signinRequest) throws CustomStreamException {
		logger.info("Received request to generate the tokens");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Get the user details for token generation process");
			User user = userRepository.findByUsername(signinRequest.getUsername().trim());
			if (user != null && passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
				logger.info("Get the access and refresh token from jsonwebtoken application");
				String tokenGenerationUrl = "http://localhost:8083/auth/jwt/generateToken";
				RestTemplate restTemplate = new RestTemplate();

				TokenPropertiesPOJO tokenPropertiesRequest = new TokenPropertiesPOJO();
				tokenPropertiesRequest.setUniqueId(user.getUniqueId());
				tokenPropertiesRequest.setSecretKey(secretKey);
				tokenPropertiesRequest.setAccessTokenTime(accessTokenValidity);
				tokenPropertiesRequest.setRefreshTokenTime(refreshTokenValidity);
						
				HttpEntity<TokenPropertiesPOJO> requestEntity = new HttpEntity<>(tokenPropertiesRequest);

				logger.info("Token are saved and display in response");
				return restTemplate.exchange(tokenGenerationUrl, HttpMethod.POST, requestEntity, ResponsePOJO.class);
			} else {
				logger.warn("Please given the proper credentials");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responsePOJO.response("Invalid Credential"));
			}
		} catch (Exception e) {
			logger.error("Unable to received the token details");
			throw new CustomStreamException("Unable to received the token details");
		}
	}

	/**
	 * This method is used to validate the token is expiry or not
	 * 
	 * @param accessToken
	 * @param tokenValidationRequest
	 * @return ResponseEntity<String>
	 */
	public ResponseEntity<String> validateToken(String accessToken) {
		logger.info("Received the request to validate the token API");
		try {
			logger.info("Pass the token and unique id in request");
			String apiUrl = "http://localhost:8083/auth/jwt/validateToken";
			
			RestTemplate restTemplate = new RestTemplate();

			if (accessToken != null && accessToken.startsWith("Bearer ")) {
				logger.info("Split the Bearer key in access token");
				accessToken = accessToken.substring(7);
			}
			
			TokenPropertiesPOJO tokenPropertiesRequest = new TokenPropertiesPOJO();
			tokenPropertiesRequest.setSecretKey(secretKey);
			tokenPropertiesRequest.setAccessToken(accessToken);

			HttpEntity<TokenPropertiesPOJO> requestEntity = new HttpEntity<>(tokenPropertiesRequest);

			logger.info("Token expiration succesfully verified using validate token API");
			return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Invalid token and user");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user and token");
		}
	}

	/**
	 * This method is used to regenerate the access and refresh token
	 * 
	 * @param refreshToken
	 * @param tokenValidationRequest
	 * @return ResponseEntity<ResponsePOJO>
	 */
	public ResponseEntity<ResponsePOJO> regenerateTokens(String refreshToken,TokenRegenerationRequestPOJO tokenRegenerationRequest) {
		logger.info("Received request to regenerate the tokens");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Regenerate tokens process is started");
			TokenPropertiesPOJO tokenPropertiesRequest = new TokenPropertiesPOJO();
			String regenerateTokenURL = "http://localhost:8083/auth/jwt/regenerateTokens";

			User user = userRepository.findByUsername(tokenRegenerationRequest.getUsername().trim());
			if (user != null ) {
				RestTemplate restTemplate = new RestTemplate();

				HttpHeaders headers = new HttpHeaders();

				headers.set("Authorization", refreshToken);
				headers.set("Content-Type", "application/json");

				if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
					logger.info("Split the Bearer key in access token");
					refreshToken = refreshToken.substring(7);
				}

				tokenPropertiesRequest.setUniqueId(user.getUniqueId());
				tokenPropertiesRequest.setSecretKey(secretKey);
				tokenPropertiesRequest.setAccessToken(tokenRegenerationRequest.getAccessToken());
				tokenPropertiesRequest.setRefreshToken(refreshToken);
				tokenPropertiesRequest.setAccessTokenTime(accessTokenValidity);
				tokenPropertiesRequest.setRefreshTokenTime(refreshTokenValidity);

				HttpEntity<TokenPropertiesPOJO> requestEntity = new HttpEntity<>(tokenPropertiesRequest, headers);

				logger.info("Regenerate tokens request verified using regenerateTokens API");
				return restTemplate.exchange(regenerateTokenURL, HttpMethod.POST, requestEntity, ResponsePOJO.class);
			} else {
				logger.error("Given name does not match database");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						     .body(responsePOJO.response("Given name does not match database"));
			}
		} catch (Exception e) {
			logger.error("Unable to regenerate the tokens");
			throw new CustomStreamException("Unable to regenerate the tokens");
		}

	}

}
