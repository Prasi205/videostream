package com.tm.videostream.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.tm.videostream.entity.Video;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.repository.VideoRepository;
import com.tm.videostream.service.VideoService;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

/**This class provides the implementation of the VideoService interface.
 * It contains methods to handles save video details, list the videos and streaming the video.
 */
@Service
public class VideoServiceImpl implements VideoService{

	Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
	
	@Autowired
	private VideoRepository videoRepository;
	
	@Value("${file.upload-dir}")
	private String uploadDir;
	
	/**This method is used to save the video details in database
	 * @param file
	 * @param title
	 * @param description
	 * @return String 
	 */
	public ResponseEntity<String> saveVideoDetails(MultipartFile file, String title, String description) {
		logger.info("Received the request to save the video details");
		try {
			logger.info("Gathering the video details from request");
			String originalFilename = file.getOriginalFilename();
			String filePath = Paths.get(uploadDir, originalFilename).toString();
			Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

			Video saveVideo = new Video();

			saveVideo.setTitle(title);
			saveVideo.setDescription(description);
			saveVideo.setFilename(originalFilename);

			videoRepository.save(saveVideo);
			logger.info("Video details are saved in database");
			return ResponseEntity.ok("Video details are saved in database");

		} catch (Exception e) {
			logger.error("Unable to save the video details in database");
			throw new CustomStreamException("Unable to save the video details in database");
		}

	}
	
	/**This method is used to list the videos from database
	 * @return List
	 */
	public List<Video> fetchVideoList() {
		logger.info("Recived the request to fecth and display the all videos");
		try {
			List<Video> videoList = videoRepository.findAll();
			logger.info("Videos are fetched");
			return videoList;
		} catch (Exception e) {
			logger.error("Unable to fetch the video list");
			throw new CustomStreamException("Unable to fetch the video list");
		}
	}
	
	/**This method is used to get the content type(Ex:mp4.avi etc) from file
	 * @param filename
	 * @return String
	 */
	private String getContentType(String filename) {
		logger.info("Get the content type for file");
	    try {
	    	String isContentType=Files.probeContentType(Paths.get(filename));
	    	logger.info("Content type is identified");
	    	return isContentType;
	    } catch (IOException e) {
	    	logger.error("Cannot get the content type for file");
	        return "Cannot get content type for file";
	    }
	}
	
	/**This method is used to streaming the video
	 * @param filename
	 * @return StreamingResponseBody
	 */
	public ResponseEntity<StreamingResponseBody> streamVideo(String filename) {
		logger.info("Received the request to streaming the video");
		try {
			logger.info("Get the file from directory");
			Path videoPath = Paths.get(uploadDir, filename);
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
					logger.error("Unable to fetch the video streaming content");
					throw new CustomStreamException("Unable to fetch the video streaming content");
				}
			};
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(getContentType(filename)));
			logger.info("Video is streamed");
			return new ResponseEntity<>(responseBody, headers, HttpStatus.OK);

		} catch (Exception e) {
			logger.error("Unable to streaming the video");
			throw new CustomStreamException("Unable to streaming the video");
		}
	}
	
}
