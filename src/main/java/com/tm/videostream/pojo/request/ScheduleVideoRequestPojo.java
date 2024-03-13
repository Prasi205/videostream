package com.tm.videostream.pojo.request;

import java.util.List;

import com.tm.videostream.pojo.ScheduleVideoPOJO;

public class ScheduleVideoRequestPojo {

	private String username;
	private List<ScheduleVideoPOJO> scheduleVideoPojo;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<ScheduleVideoPOJO> getScheduleVideoPojo() {
		return scheduleVideoPojo;
	}

	public void setSchduleVideoPojo(List<ScheduleVideoPOJO> scheduleVideoPojo) {
		this.scheduleVideoPojo = scheduleVideoPojo;
	}

}
