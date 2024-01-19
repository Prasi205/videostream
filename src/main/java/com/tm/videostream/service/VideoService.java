package com.tm.videostream.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.request.VideoDetailsRequest;

public interface VideoService {

	public boolean saveVideoDetails(MultipartFile file, String title, String description,String username);

	public ResponseEntity<StreamingResponseBody> streamingVideoByFileName(String filename);
	
	public boolean updateApprovalStatusByFileId(int fileId, String approvalStatus,String username);
	
	public List<VideoDTO> fetchVideoByApprovalStatus(String username, String approvalStatus,String search);
	
	public List<VideoDTO> fetchVideoByOption(String search,String approvalStatus,String username);
	
	public List<VideoDTO> fetchVideoDetails(VideoDetailsRequest videoDetailsRequest);

}
