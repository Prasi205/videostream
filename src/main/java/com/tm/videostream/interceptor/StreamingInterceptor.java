package com.tm.videostream.interceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.service.UserService;

@Component
public class StreamingInterceptor implements HandlerInterceptor {

	Logger logger = LoggerFactory.getLogger(StreamingInterceptor.class);

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			String contentType = request.getContentType();
			if(DispatcherType.ASYNC == request.getDispatcherType()) {
				return true;
			}
			
			
			logger.info("Received request is authorized request");
			System.out.println(request.getMethod());
			System.out.println(request.getDispatcherType());
			
	        String header = request.getHeader("AUTHORIZATION");
			TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();

			if (contentType != null && contentType.startsWith("multipart/form-data")) {
				logger.info("Content Type: {}", contentType);
				String username = request.getParameter("username");
				tokenValidationRequest.setUsername(username);
			} else if (contentType != null && contentType.startsWith("application/json")) {
				logger.info("Content Type: {}", contentType);
				StringBuilder stringBuilder=new StringBuilder();
				
			    String line;
			    while ((line = request.getReader().readLine()) != null) {
			    	stringBuilder.append(line);
			    }
 			    JSONObject jsonObject = new JSONObject(stringBuilder.toString());

			    String username=jsonObject.getString("username");
 
				tokenValidationRequest.setUsername(username);
				
//				JsonNode jsonNode = objectMapper.readTree(request.getReader());
//				String username = jsonNode.get("username").asText();
//				tokenValidationRequest.setUsername(username);
			}
			
			ResponseEntity<String> validationResponse = userService.validateToken(header, tokenValidationRequest);
			if (validationResponse.getStatusCode() == HttpStatus.OK) {
				logger.info("Received response is 200");
				return true;
			} else {
				logger.error("Invalid user and token");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//				response.getWriter().write("Invalid User and token");
				return false;
			}
		} catch (Exception exception) {
			logger.error("Unable to validate the request");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//			response.getWriter().write("Invalid User and token");
			return false;
		}
	}

}
