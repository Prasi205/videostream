package com.tm.videostream.service.impl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.tm.videostream.entity.Roles;
import com.tm.videostream.entity.User;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.RoleRequestPOJO;
import com.tm.videostream.pojo.UserRequestPOJO;
import com.tm.videostream.repository.RoleRepository;
import com.tm.videostream.repository.UserRepository;
import com.tm.videostream.request.SigninRequest;
import com.tm.videostream.request.TokenPropertiesRequest;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.response.JwtResponsePOJO;
import com.tm.videostream.service.UserService;

@Service
public class UserServiceImpl implements UserService,UserDetailsService{

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
	
	/**This method is used to encoded the user password
	 * @param password
	 * @return String
	 */
	public String getEncodedPassword(String password) {
		try {
			logger.info("Password is encoded");
			return passwordEncoder.encode(password);
		} catch (Exception e) {
			logger.error("Unable to encoded the password");
			return "Unable to encoded the password";
		}	
	}
	
	/**This method is used to save the roll details in database
	 * @param roleRequestPojo
	 * @return String
	 */
	public String saveRollDetails(RoleRequestPOJO roleRequestPojo){
		logger.info("Received request to save the role details");
		try {
			Roles saveRollDetails=new Roles();
			saveRollDetails.setRoleName("ROLE_"+roleRequestPojo.getRoleName());
			roleRepository.save(saveRollDetails);
			logger.info("Roles details are added in database");
			return "Roles details are added in database";
		} catch (Exception e) {
			logger.error("Unable to save roll details in database");
			return "Unable to save roll details in database";
		}
	}
	
	/**This method is used to save the user details in database
	 * @param userRequestPOJO
	 * @return String
	 */
	public String saveSignUPDetails(UserRequestPOJO userRequestPOJO) {
		logger.info("Received request to save the user details in database");
		try {
			Roles roles=new Roles();
			User user=new User();
			
			user.setName(userRequestPOJO.getName());
			user.setEmail(userRequestPOJO.getEmail());
			user.setUsername(userRequestPOJO.getUsername());
			user.setPassword(getEncodedPassword(userRequestPOJO.getPassword()));
			
			roles.setRoleId(1);
			user.setRoles(roles);
	
			userRepository.save(user);
			logger.info("User details saved in database");
			
			return "User details are saved in database";
		} catch (Exception e) {
			logger.error("Unable to save the user details");
			return "Unable to save the roll details";
		}
		
	}
	
	/**This method is used to load the user details based on username
	 * @param username
	 * @return UserDetails
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.info("Received request to find the user details based on user name");
		User user = userRepository.findByUsername(username);
		if (user != null) {
			logger.info("Pass the username, password in userdetails for valid user");
			return new org.springframework.security.core.userdetails.User(user.getUsername(), 
					        user.getPassword(), Collections.emptyList());
		} else {
			logger.error("user not found with username");
			throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}
	
	/**This method is used to generate the tokens and return in response
	 * @param signinRequest
	 * @return JwtResponsePOJO
	 */
	public ResponseEntity<JwtResponsePOJO> generateTokens(SigninRequest signinRequest) throws CustomStreamException{
		logger.info("Received request to generate the tokens");
		try {
			logger.info("Get the user details for token generation process");
			User user = userRepository.findByUsername(signinRequest.getUsername());
			if (passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
				logger.info("Get the access and refresh token from jsonwebtoken application");
				String tokenGenerationUrl="http://localhost:8083/auth/jwt/generateToken";
				RestTemplate restTemplate =new RestTemplate();
		        
	            TokenPropertiesRequest tokenPropertiesRequest = new TokenPropertiesRequest(
	            		user.getUserId(),
	            		secretKey,
	                    accessTokenValidity,
	                    refreshTokenValidity);
	            
	            HttpEntity<TokenPropertiesRequest> requestEntity = new HttpEntity<>(tokenPropertiesRequest);
	            
		        logger.info("Token are saved and display in response");
		        return restTemplate.exchange(tokenGenerationUrl, HttpMethod.POST, requestEntity, JwtResponsePOJO.class);
			}else {
				logger.error("Please given the proper credentials");
				throw new CustomStreamException("Invalid credential");
			}
		} catch (Exception e) {
			logger.error("Unable to received the token details");
		    throw new CustomStreamException("Unable to received the token details");
		}
	}	
	
	/**This method is used to validate the token is expiry or not
	 * @param accessToken
	 * @param tokenValidationRequest
	 * @return String
	 */
	public ResponseEntity<String> validateToken(String accessToken, String username) {
		logger.info("Received the request to validate the token API");
		try {
			logger.info("Pass the token and unique id in request");
			String apiUrl = "http://localhost:8083/auth/jwt/validateToken";
			
			User user = userRepository.findByUsername(username);
		    RestTemplate restTemplate = new RestTemplate();
		    
		    if (accessToken != null && accessToken.startsWith("Bearer ")) {
		    	logger.info("Split the Bearer key in access token");
		    	accessToken = accessToken.substring(7);
	        }
		    
		    TokenPropertiesRequest tokenPropertiesRequest=new TokenPropertiesRequest();
		    tokenPropertiesRequest.setUniqueId(user.getUserId());
		    tokenPropertiesRequest.setSecretKey(secretKey);
		    tokenPropertiesRequest.setAccessToken(accessToken);
		    
		    HttpEntity<TokenPropertiesRequest> requestEntity = new HttpEntity<>(tokenPropertiesRequest);

		    logger.info("Token expiration succesfully verified using validate token API");
		    return restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
		} catch (Exception e) {
			logger.error("Invalid token and user");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid user and token");
		}		
		
	}
	
	/**This method is used to regenerate the access and refresh token
	 * @param refreshToken
	 * @param tokenValidationRequest
	 * @return JwtResponsePOJO
	 */
	public ResponseEntity<JwtResponsePOJO> regenerateTokens(String refreshToken, TokenValidationRequest tokenValidationRequest) {
		logger.info("Received request to regenerate the tokens");
		try {
			logger.info("Regenerate tokens process is started");
			TokenPropertiesRequest tokenPropertiesRequest = new TokenPropertiesRequest();
			String regenerateTokenURL = "http://localhost:8083/auth/jwt/regenerateTokens";

			User user = userRepository.findByUsername(tokenValidationRequest.getUsername());
			RestTemplate restTemplate = new RestTemplate();

			HttpHeaders headers = new HttpHeaders();

			headers.set("Authorization", refreshToken);
			headers.set("Content-Type", "application/json");
			    
			if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
				logger.info("Split the Bearer key in access token");
				refreshToken = refreshToken.substring(7);
			}
			 
			tokenPropertiesRequest.setUniqueId(user.getUserId());
			tokenPropertiesRequest.setSecretKey(secretKey);
			tokenPropertiesRequest.setAccessToken(tokenValidationRequest.getAccessToken());
			tokenPropertiesRequest.setRefreshToken(refreshToken);
			tokenPropertiesRequest.setAccessTokenTime(accessTokenValidity);
			tokenPropertiesRequest.setRefreshTokenTime(refreshTokenValidity);

			HttpEntity<TokenPropertiesRequest> requestEntity = new HttpEntity<>(tokenPropertiesRequest,headers);

			logger.info("Access and refresh tokens is regenerated");
   			return restTemplate.exchange(regenerateTokenURL, HttpMethod.POST, requestEntity, JwtResponsePOJO.class);
		} catch (Exception e) {
			logger.error("Unable to regenerate the tokens");
			throw new CustomStreamException("Unable to regenerate the tokens");
		}

	}
	
}
