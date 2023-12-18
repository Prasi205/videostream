package com.tm.videostream.request;

public class TokenPropertiesRequest {

	private int uniqueId;
	private String secretKey;
	private int accessTokenTime;
	private int refreshTokenTime;

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
