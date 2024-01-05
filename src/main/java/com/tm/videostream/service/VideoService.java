package com.tm.videostream.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

public interface VideoService {

	public boolean saveVideoDetails(MultipartFile file, String title, String description);

	public ResponseEntity<StreamingResponseBody> streamingVideoByFileName(String filename);

}
