package com.tm.videostream.dto;

import java.time.LocalDateTime;

public class ScheduleVideoDTO {

	private int scheduleId; 
	private String zoneId;
	private String filename;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	
	public ScheduleVideoDTO(int scheduleId, String zoneId, String filename, LocalDateTime startTime,
			LocalDateTime endTime) {
		super();
		this.scheduleId = scheduleId;
		this.zoneId = zoneId;
		this.filename = filename;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public int getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(int scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

}