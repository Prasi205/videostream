package com.tm.videostream.interceptor;

import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.service.UserService;

@Component
public class StreamingInterceptor implements HandlerInterceptor {

	Logger logger = LoggerFactory.getLogger(StreamingInterceptor.class);

	@Autowired
	private UserService userService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		try {
			if (DispatcherType.REQUEST != request.getDispatcherType()) {
				return true;
			}

			logger.info("Received request is authorized request");

			String header = request.getHeader("AUTHORIZATION");
			TokenValidationRequest tokenValidationRequest = new TokenValidationRequest();

			String username = request.getParameter("username");
			if (username.trim().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				response.getWriter().write("Username cannot be blank");
				return false;
			} else {
				tokenValidationRequest.setUsername(username);
			}

			ResponseEntity<String> validationResponse = userService.validateToken(header, tokenValidationRequest);
			if (validationResponse.getStatusCode() == HttpStatus.OK) {
				logger.info("Received response is 200");
				return true;
			} else {
				logger.error("Invalid user and token");
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				response.getWriter().write("Invalid User and token");
				return false;
			}
			
		} catch (Exception exception) {
			logger.error("Unable to validate the request");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.getWriter().write("Invalid User and token");
			return false;
		}

	}

}
