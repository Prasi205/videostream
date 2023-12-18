package com.tm.videostream.service.impl;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
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
import com.tm.videostream.repository.RoleRepository;
import com.tm.videostream.repository.UserRepository;
import com.tm.videostream.request.SigninRequest;
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
	private String SECRET_KEY;
	
	@Value("${jwt.accessTokenValidity}")
	private int ACCESS_TOKEN_VALIDITY;
	
	@Value("${jwt.refreshTokenValidity}")
	private int REFRESH_TOKEN_VALIDITY;
	
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
	 * @param roles
	 * @return String
	 */
	public ResponseEntity<String> saveRollDetails(Roles roles){
		logger.info("Received request to save the role details");
		try {
			roles.setRoleName("ROLE_" + roles.getRoleName());
			roleRepository.save(roles);
			return ResponseEntity.ok("Roles details are added in database");
		} catch (Exception e) {
			logger.info("Unable to save roll details in database");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to save roll details in database");
		}
	}
	
	/**This method is used to save the user details in database
	 * @param user
	 * @return String
	 */
	public ResponseEntity<String> saveUserDetails(User user) {
		logger.info("Received request to save the user details in database");
		try {
			Roles roles=new Roles();
			
			user.setPassword(getEncodedPassword(user.getPassword()));
			roles.setRoleId(1);
			user.setRoles(roles);
	
			userRepository.save(user);
			logger.info("User details saved in database");
			
			return ResponseEntity.ok("User details are saved in database");
		} catch (Exception e) {
			logger.error("Unable to save the user details");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unable to save the roll details");
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
	 * @return SigninResponse
	 */
	public ResponseEntity<JwtResponsePOJO> generateTokens(SigninRequest signinRequest) throws CustomStreamException{
		logger.info("Received request to generate the tokens");
		try {
			UserDetails userDetails = loadUserByUsername(signinRequest.getUsername());
			if (passwordEncoder.matches(signinRequest.getPassword(), userDetails.getPassword())) {
				logger.info("Get the access and refresh token from jwtutil class");
				String tokenGenerationUrl="http://localhost:8083/auth/jwt/generateToken";
				RestTemplate restTemplate =new RestTemplate();
		        HttpEntity<SigninRequest> requestEntity = new HttpEntity<>(signinRequest);
		        User user=userRepository.findByUsername(signinRequest.getUsername());
		        setTokenProperties(signinRequest,user);
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
	
	/**This method is used to set the id,secretkey, token time in request
	 * @param signinRequest
	 * @param user
	 */
	private void setTokenProperties(SigninRequest signinRequest, User user) {
		logger.info("Recived request to set the user id, secret key,token time");
		try {
			signinRequest.setUniqueId(user.getUserId());
		    signinRequest.setSecretKey(SECRET_KEY);
		    signinRequest.setAccessTokenTime(ACCESS_TOKEN_VALIDITY);
		    signinRequest.setRefreshTokenTime(REFRESH_TOKEN_VALIDITY);
		    logger.info("The Details are is setted");
		} catch (Exception e) {
			logger.error("Unable to set the details");
			throw new CustomStreamException("Unable to set the details");
		}
	    
	}
	
	
}
