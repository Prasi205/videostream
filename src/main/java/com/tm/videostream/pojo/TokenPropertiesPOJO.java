package com.tm.videostream.pojo;

public class TokenPropertiesPOJO {

	private String uniqueId;
	private String secretKey;
	private int accessTokenTime;
	private int refreshTokenTime;
	private String accessToken;
	private String refreshToken;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getAccessTokenTime() {
		return accessTokenTime;
	}

	public void setAccessTokenTime(int accessTokenTime) {
		this.accessTokenTime = accessTokenTime;
	}

	public int getRefreshTokenTime() {
		return refreshTokenTime;
	}

	public void setRefreshTokenTime(int refreshTokenTime) {
		this.refreshTokenTime = refreshTokenTime;
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
