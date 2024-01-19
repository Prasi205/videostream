package com.tm.videostream.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.request.VideoDetailsRequest;
import com.tm.videostream.response.ResponsePOJO;
import com.tm.videostream.service.VideoService;

/**
 * This class handles the video related operations like saving video,list the
 * videos and streaming the videos
 */
@RestController
@RequestMapping("/customer")
public class VideoController {

	Logger logger = LoggerFactory.getLogger(VideoController.class);

	@Autowired
	private VideoService videoService;

	/**
	 * Handles the video details saving process based on the received request
	 * 
	 * @param file
	 * @param title
	 * @param description
	 * @return ResponsePOJO
	 */
	@PostMapping("/saveVideoDetails")
	public ResponsePOJO saveVideoDetails(@RequestParam MultipartFile file, @RequestParam String title,
			@RequestParam String description,@RequestParam String username) {
		logger.info("Received the request to save the video details");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video details saving request is received");
			if (videoService.saveVideoDetails(file, title, description, username)) {
				logger.info("Video details are saved in database");
				responsePOJO.response("Video details are saved in database", null, true);
			} else {
				logger.error("Unable to save the video details");
				responsePOJO.response("Unable to save the video details", null, false);
			}
		} catch (Exception e) {
			logger.error("Unable to validate the request");
			throw new CustomStreamException("Unable to received the video details saving request");
		}
		return responsePOJO;
	}

	/**
	 * Handles the video streaming process based on received request
	 * 
	 * @param filename
	 * @return StreamingResponseBody
	 */
	@PostMapping("/streamingVideo")
	public ResponseEntity<StreamingResponseBody> streamingVideo(@RequestParam String filename) {
		logger.info("Received the request to play the video");
		ResponseEntity<StreamingResponseBody> streamingVideo;
		try {
			logger.info("Video Streaming or playing request is received");
			streamingVideo = videoService.streamingVideoByFileName(filename);
		} catch (Exception e) {
			logger.error("Unable to streaming or play the video");
			throw new CustomStreamException("Unable to streaming or play the video");
		}
		return streamingVideo;
	}

	/**
	 * Handles the approval status update process based on received request
	 * 
	 * @param fileId
	 * @param approvalStatus
	 * @return ResponsePOJO
	 */
	@PostMapping("/updateApprovalStatus")
	public ResponsePOJO updateApprovalStatus(@RequestParam int fileId, @RequestParam String approvalStatus,
			@RequestParam String username) {
		logger.info("Received request to update the approval status");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			if (isParamValidation(approvalStatus)) {
				if (videoService.updateApprovalStatusByFileId(fileId, approvalStatus, username)) {
					logger.info("Approval status is changed for request fileId in database");
					responsePOJO.response("Approval status has been changed", null, true);
				} else {
					logger.error("Unable to change the approval status");
					responsePOJO.response("Unable to change the approval status", null, false);
				}
			} else {
				logger.warn("Given field cannot blank");
				responsePOJO.response("Given field cannot blank", null, false);
			}

		} catch (Exception e) {
			logger.error("Unable to validate the request");
			throw new CustomStreamException("Unable to received the video details saving request");
		}
		return responsePOJO;
	}

	/**
	 * Handles the video list fetch process based on received request
	 * 
	 * @param approvalStatus
	 * @param username
	 * @return List<VideoDTO>
	 */
	@PostMapping("/fetchApprovalStatusVideo")
	public ResponsePOJO fetchVideoByApprovalStatus(@RequestParam(required = false) String search,
			@RequestParam String approvalStatus, @RequestParam String username) {
		logger.info("Received request to fetch the video's on role based");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video fetch request is received");
			if (isParamValidation(approvalStatus) && !search.trim().isEmpty()) {
				List<VideoDTO> videoDTOList = videoService.fetchVideoByApprovalStatus(search, approvalStatus, username);
				if (videoDTOList.isEmpty()) {
					logger.info("No videos found for user and status");
					responsePOJO.response("No videos found for user and  status", null, false);
				} else {
					logger.info("Video's fetched successfully on role and status");
					responsePOJO.response("Video's fetched successfully on role and status", videoDTOList, true);
				}
			} else {
				logger.error("Given field's cannot blank");
				responsePOJO.response("Given field's cannot blank", null, false);
			}
		} catch (Exception e) {
			logger.error("Unable to fetch the video details");
			throw new CustomStreamException("Unable to fetch the video details");
		}
		return responsePOJO;
	}

	/**
	 * Handles the video list fetch process based on received request
	 * 
	 * @param approvalStatus
	 * @param username
	 * @return List<VideoDTO>
	 */
	@PostMapping("/fetchVideoDetails")
	public ResponsePOJO fetchVideoDetails(@RequestBody @Valid VideoDetailsRequest videoDetailsRequest) {
		logger.info("Received request to fetch the video's on role based");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video fetch request is received");
			List<VideoDTO> videoDTOList = videoService.fetchVideoDetails(videoDetailsRequest);
			if (videoDTOList.isEmpty()) {
				logger.info("No videos found for user and approval status");
				responsePOJO.response("No videos found for user and approval status", null, false);
			} else {
				logger.info("Video's fetched successfully on role based");
				responsePOJO.response("Video's fetched successfully on role based", videoDTOList, true);
			}
		} catch (Exception e) {
			logger.error("Unable to fetch the video's");
			throw new CustomStreamException("Unable to fetch the video's");
		}
		return responsePOJO;
	}

	public boolean isParamValidation(String approvalStatus) {
		logger.info("Param validation request is received");
		try {
			logger.info("Checking approval status is blank or not blank");
			return !approvalStatus.trim().isEmpty();
		} catch (Exception e) {
			logger.error("Unable to validate the field");
			throw new CustomStreamException("Unable to validate the field");
		}
	}
}
