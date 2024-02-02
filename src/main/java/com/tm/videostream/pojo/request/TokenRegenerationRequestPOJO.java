package com.tm.videostream.pojo.request;

import javax.validation.constraints.NotBlank;

public class TokenRegenerationRequestPOJO {

	@NotBlank(message =  "Username cannot be blank")
	private String username;
	
	@NotBlank(message =  "Access token cannot be blank")
	private String accessToken;

	private String refreshToken;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
