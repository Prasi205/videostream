package com.tm.videostream.request;

public class TokenPropertiesRequest {

	private int uniqueId;
	private String secretKey;
    private String accessToken;
    private String refreshToken;
	private int accessTokenTime;
	private int refreshTokenTime;

	public TokenPropertiesRequest() {
		super();
	}

	public TokenPropertiesRequest(int uniqueId, String secretKey, int accessTokenTime, int refreshTokenTime) {
		super();
		this.uniqueId = uniqueId;
		this.secretKey = secretKey;
		this.accessTokenTime = accessTokenTime;
		this.refreshTokenTime = refreshTokenTime;
	}

	public int getUniqueId() {
		return uniqueId;
	}

	public 	void setUniqueId(int uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
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

}
