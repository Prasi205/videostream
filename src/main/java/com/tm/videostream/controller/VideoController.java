package com.tm.videostream.controller;

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

import com.tm.videostream.exception.CustomStreamException;
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
			@RequestParam String description) {
		logger.info("Received the request to save the video details");
		ResponsePOJO responsePOJO = new ResponsePOJO();
		try {
			logger.info("Video details saving request is received");
			if (videoService.saveVideoDetails(file, title, description)) {
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

}
