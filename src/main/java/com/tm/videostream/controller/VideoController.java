package com.tm.videostream.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.response.ResponsePOJO;
import com.tm.videostream.service.VideoService;

/**
 * This class handles the video related operations like saving video,list the
 * videos based on role,approval status,options and streaming the videos
 */
@RestController
@RequestMapping("/user")
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
			@RequestParam String description, @RequestParam String username) {
		logger.info("Received the request to save the video details");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video details saving request is received");
			if (title.trim().isEmpty() || description.trim().isEmpty()) {
				responsePOJO.response("Given field cannot be blank", null, false);
			} else {
				if (videoService.saveVideoDetails(file, title, description, username)) {
					logger.info("Video details are saved in database");
					responsePOJO.response("Video details are saved in database", null, true);
				} else {
					logger.error("Unable to save the video details");
					responsePOJO.response("Unable to save the video details", null, false);
				}
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
	 * Handles the video's fetch process based on received request
	 * 
	 * @param approvalStatus
	 * @param username
	 * @return List<VideoDTO>
	 */
	@PostMapping(value = "/fetchVideoDetails")
	public ResponsePOJO fetchVideoDetails(@RequestParam(required = false) String search,
			@RequestParam String approvalStatus, @RequestParam String username) {
		logger.info("Received request to fetch the video's on role based");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video fetch request is received");
			if (isParamValidation(approvalStatus)) {
				List<VideoDTO> videoDTOList = videoService.fetchVideoDetails(search, approvalStatus, username);
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

	/**Handles the users video's fetch process based on received request
	 * @param approvalStatus
	 * @param username
	 * @return ResponsePOJO
	 */
	@PostMapping("/fecthUsersVideo")
	public ResponsePOJO fetchVideoWithoutCurrentUserVideo(@RequestParam String approvalStatus, 
			                @RequestParam String username) {
		logger.info("Received request to fetch the video's on role based");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video fetch request is received");
			if (isParamValidation(approvalStatus)) {
				List<VideoDTO> videoDTOList = videoService.fetchVideoWithoutCurrentUserVideo(approvalStatus, username);
				if (videoDTOList.isEmpty()) {
					logger.info("User's video cannot be found");
					responsePOJO.response("Users video cannot be found", null, false);
				} else {
					logger.info("Users video are fetched successfully");
					responsePOJO.response("Users video are fetched successfully", videoDTOList, true);
				}
			} else {
				logger.error("All fields are mantadory");
				responsePOJO.response("All fields are mantadory", null, false);
			}
		} catch (Exception e) {
			logger.error("Unable to fetch the users video details");
			throw new CustomStreamException("Unable to fetch the users video details");
		}
		return responsePOJO;
	}
	
	/**Validate the approval status is not blank
	 * @param approvalStatus
	 * @return boolean
	 */
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
