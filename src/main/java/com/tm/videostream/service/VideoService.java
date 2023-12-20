package com.tm.videostream.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.entity.Video;

public interface VideoService {
	
	public ResponseEntity<String> saveVideoDetails(MultipartFile file, String title, String description);
	
	public List<Video> fetchVideoList();
	
	public ResponseEntity<StreamingResponseBody> streamVideo(String filename);

}
