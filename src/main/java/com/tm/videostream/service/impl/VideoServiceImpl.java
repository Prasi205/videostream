package com.tm.videostream.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.entity.Video;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.repository.VideoRepository;
import com.tm.videostream.service.VideoService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * This class provides the implementation of the VideoService interface. It
 * contains methods to handles save video details, list the videos and streaming
 * the video.
 */
@Service
public class VideoServiceImpl implements VideoService {

	Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

	@Autowired
	private VideoRepository videoRepository;

	/**
	 * This method is used to save the video details in database
	 * 
	 * @param file
	 * @param title
	 * @param description
	 * @return boolean
	 */
	public boolean saveVideoDetails(MultipartFile file, String title, String description) {
		logger.info("Received the request to save the video details");
		try {
			logger.info("Gathering the video details from request");

			String projectDirectory = System.getProperty("user.dir");
			String videoDirectory = projectDirectory + "/Videos/";

			Path directory = Paths.get(videoDirectory);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}

			String filePath = Paths.get(videoDirectory, file.getOriginalFilename()).toString();
			Files.copy(file.getInputStream(), Paths.get(filePath));

			Video saveVideo = new Video();

			saveVideo.setTitle(title);
			saveVideo.setDescription(description);
			saveVideo.setFilename(file.getOriginalFilename());
			saveVideo.setApprovalStatus("NEW");
			saveVideo.setCreatedAt(new Date());
			saveVideo.setUpdatedAt(new Date());

			videoRepository.save(saveVideo);
			logger.info("Video details are saved in database");
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Unable to save the video details in database");
			return false;
		}

	}

	/**
	 * This method is used to get the content type(Ex:mp4.avi etc) from file
	 * 
	 * @param filename
	 * @return String
	 */
	private String getContentType(String filename) {
		logger.info("Get the content type for file");
		try {
			String isContentType = Files.probeContentType(Paths.get(filename));
			logger.info("Content type is identified");
			return isContentType;
		} catch (IOException e) {
			logger.error("Cannot get the content type for file");
			return "Cannot get content type for file";
		}
	}

	/**
	 * This method is used to streaming the video
	 * 
	 * @param filename
	 * @return ResponseEntity<StreamingResponseBody>
	 */
	public ResponseEntity<StreamingResponseBody> streamingVideoByFileName(String filename) {
		logger.info("Received the request to streaming the video");
		try {
			logger.info("Get the file from directory");

			String projectDirectory = System.getProperty("user.dir");
			String videoDirectory = projectDirectory + "/Videos/";

			Path videoPath = Paths.get(videoDirectory, filename);
			Resource videoResource = new FileSystemResource(videoPath.toFile());
			logger.info("Valid token, Next go to video streaming process");
			StreamingResponseBody responseBody = outputStream -> {
				try (InputStream inputStream = videoResource.getInputStream()) {
					logger.info("Get the input stream content from video");
					byte[] buffer = new byte[4096];
					int bytesRead;
					while ((bytesRead = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, bytesRead);
						outputStream.flush();
					}
				} catch (IOException e) {
					logger.error("Unable to fetch the video streaming content", e);
					throw new CustomStreamException("Unable to fetch the video streaming content");
				}
			};
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(getContentType(filename)));
			headers.set("Accept-Ranges", "bytes");

			logger.info("Video is streamed");
			return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Unable to streaming the video");
			throw new CustomStreamException("Unable to streaming the video");
		}
	}

}
