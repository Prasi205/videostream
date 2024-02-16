package com.tm.videostream.dto;

public class VideoDTO {

	private int fileId;
	private String title;
	private String description;
	private String filename;
	private float duration;
	private String username;
	
	public VideoDTO(int fileId, String title, String description, String filename, float duration, String username) {
		super();
		this.fileId = fileId;
		this.title = title;
		this.description = description;
		this.filename = filename;
		this.duration = duration;
		this.username = username;
	}

	public int getFileId() {
		return fileId;
	}

	public void setFileId(int fileId) {
		this.fileId = fileId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public float getDuration() {
		return duration;
	}

	public void setDuration(float duration) {
		this.duration = duration;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
