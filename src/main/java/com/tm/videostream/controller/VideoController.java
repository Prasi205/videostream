package com.tm.videostream.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.entity.Video;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.service.VideoService;

@RestController
public class VideoController {
	
	Logger logger = LoggerFactory.getLogger(VideoController.class);

	@Autowired
	private VideoService videoService;
	
	/**Handles the video details saving process based on the received request
	 * @param file
	 * @param title
	 * @param description
	 * @param username
	 * @param token
	 * @return String
	 */
	@PostMapping("/saveVideoDetails")
	public ResponseEntity<String> saveVideoDetails(@RequestParam("file") MultipartFile file, @RequestParam String title,
                                     @RequestParam String description){
		logger.info("Received the request to save the video details");
		ResponseEntity<String> isSavedVideoDetails;
		try {
			logger.info("Video details saving request is received");
			isSavedVideoDetails=videoService.saveVideoDetails(file, title, description);
		} catch (Exception e) {
			logger.error("Unable to validate the request");
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unable to validate the request");
		}
		return isSavedVideoDetails;
	}
	
	/**Handles the video list fecthing process based on received request
	 * @return List
	 */
	@PostMapping("/videoList")
	public ResponseEntity<List<Video>> fetchVideoList(){
		logger.info("Received the request to fetch the video list");
		try {
			logger.info("Video List request is received");
			List<Video> videoList=videoService.fetchVideoList();
			return ResponseEntity.ok(videoList);
		} catch (Exception e) {
			logger.error("Unable to received the fetch video list request");
			throw new CustomStreamException("Unable to received the fetch video list request");
		}
	}
	
	/**Handles the video streaming process based on received request
	 * @param filename
	 * @param username
	 * @param token
	 * @return StreamingResponseBody
	 */
	@PostMapping("/streamingVideo")
	public ResponseEntity<StreamingResponseBody> streamingVideo(@PathVariable String filename){
		logger.info("Received the request to play the video");
		ResponseEntity<StreamingResponseBody> streamingVideo;
		try {
			logger.info("Video Streaming or playing request is received");
			streamingVideo=videoService.streamVideo(filename);
		} catch (Exception e) {
			logger.error("Unable to streaming or play the video");
			throw new CustomStreamException("Unable to streaming or play the video");
		}
		return streamingVideo;
	}
	
}
