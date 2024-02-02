package com.tm.videostream.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.pojo.request.StatusUpdateRequestPOJO;
import com.tm.videostream.pojo.request.VideoDetailsRequestPOJO;

public interface VideoService {

	public boolean saveVideoDetails(MultipartFile file, String title, String description,String username);

	public ResponseEntity<StreamingResponseBody> streamingVideoByFileName(String filename);
	
	public boolean updateApprovalStatusByFileId(StatusUpdateRequestPOJO statusUpdateRequestPOJO);
	
	public List<VideoDTO> fetchVideoDetails(VideoDetailsRequestPOJO videoDetailsRequestPOJO);
	
	public List<VideoDTO> fetchVideoWithoutCurrentUserVideo(VideoDetailsRequestPOJO videoDetailsRequestPOJO);
	
	
}
