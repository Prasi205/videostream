package com.tm.videostream.request;

import javax.validation.constraints.NotBlank;

public class VideoDetailsRequest {

	private int fileId;
	
	@NotBlank(message = "Approval Status cannot be blank")
	private String approvalStatus;
	
	private String search;
	
	private String username;

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	

}
