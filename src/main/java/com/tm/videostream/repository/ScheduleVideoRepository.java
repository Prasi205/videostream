package com.tm.videostream.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.videostream.entity.ScheduleVideo;

public interface ScheduleVideoRepository extends JpaRepository<ScheduleVideo, Integer>{
	
	public List<ScheduleVideo> findByZoneId(String zoneId);

//	@Query("SELECT new com.tm.videostream.dto.ScheduleVideoDTO(scheduleVideo.scheduleId, scheduleVideo.zoneId, "
//			+ "scheduleVideo.filename, scheduleVideo.startTime, scheduleVideo.endTime ) FROM ScheduleVideo scheduleVideo "
//			+ "WHERE scheduleVideo.zoneId = :zoneId AND date_format(scheduleVideo.startTime,'%Y-%m-%d %H:%i') <= :zoneLocalTime "
//			+ "AND date_format(scheduleVideo.endTime,'%Y-%m-%d %H:%i') >= :zoneLocalTime ")
//	public ScheduleVideoDTO findZonedScheduleVideo(String zoneId, String zoneLocalTime);
//	
//	@Query("SELECT new com.tm.videostream.dto.ScheduleVideoDTO(scheduleVideo.scheduleId, scheduleVideo.zoneId, "
//			+ "scheduleVideo.filename, scheduleVideo.startTime, scheduleVideo.endTime ) FROM ScheduleVideo scheduleVideo "
//			+ "WHERE scheduleVideo.zoneId = "+ZoneConstant.DEFALULT_ZONE+" AND date_format(scheduleVideo.startTime,'%Y-%m-%d %H:%i') <= :zoneLocalTime "
//			+ "AND date_format(scheduleVideo.endTime,'%Y-%m-%d %H:%i') >= :zoneLocalTime ")
//	public ScheduleVideoDTO findAllZoneTimeScheduleVideo( String zoneLocalTime);
	
	
}
