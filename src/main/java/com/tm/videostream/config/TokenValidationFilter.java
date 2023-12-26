package com.tm.videostream.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.videostream.request.TokenValidationRequest;
import com.tm.videostream.service.UserService;

@Component
public class TokenValidationFilter extends OncePerRequestFilter {

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	Logger logger = LoggerFactory.getLogger(TokenValidationFilter.class);
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			logger.info("Check the request is authorize or unauthorize");
			if (requiresAuthentication(request.getRequestURI())) {
				logger.info("Received the request is authorizated request");
				String header = request.getHeader("AUTHORIZATION");
				String username=request.getParameter("username");
				
//				TokenValidationRequest tokenValidationRequest = objectMapper.readValue(request.getReader(),
//						          TokenValidationRequest.class);
				ResponseEntity<String> validationResponse = userService.validateToken(header, username);
				if (validationResponse.getStatusCode() == HttpStatus.OK) {
					logger.info("Received request is valid request");
					filterChain.doFilter(request, response);
				} else {
					logger.error("Received request is not valid request ");
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid user and token");
				}
			} else {
				logger.info("Return the response for no authorization request");
				filterChain.doFilter(request, response);
			}
		} catch (Exception e) {
            logger.error("Unable to validate the request");
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to validate the request");
		}

	}

	private boolean requiresAuthentication(String uri) {
		try {
			logger.info("Check the received request is not equal to no authorized api");
			return !uri.equals("/videostream/user/signin") && !uri.equals("/videostream/user/signup")
					&& !uri.equals("/videostream/user/regenerateTokens");
		} catch (Exception e) {
			logger.error("Unable to equal the requests");
			return false;
		}
		
	}

}
