package com.tm.videostream.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;

import org.apache.catalina.connector.ClientAbortException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tm.videostream.constants.RoleConstant;
import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.entity.User;
import com.tm.videostream.entity.Video;
import com.tm.videostream.exception.CustomStreamException;
import com.tm.videostream.pojo.request.FilterVideoRequest;
import com.tm.videostream.pojo.request.StatusUpdateRequestPOJO;
import com.tm.videostream.pojo.request.VideoDetailsRequestPOJO;
import com.tm.videostream.repository.UserRepository;
import com.tm.videostream.repository.VideoRepository;

import com.tm.videostream.service.VideoService;

import org.springframework.http.HttpHeaders;
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

	@Autowired
	private UserRepository userRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * This method is used to save the video details in database
	 * 
	 * @param file
	 * @param title
	 * @param description
	 * @return boolean
	 */
	public boolean saveVideoDetails(MultipartFile file, String title, String description, String username,
			          float size, float duration, MultipartFile videoThumbnail) {
		logger.info("Received the request to save the video details");
		try {
			logger.info("Gathering the video details from request");

			String projectDirectory = System.getProperty("user.dir");
			String videoDirectory = projectDirectory + "/Videos/";

			Path directory = Paths.get(videoDirectory);
			if (!Files.exists(directory)) {
				Files.createDirectories(directory);
			}
			
			String videoExtension = StringUtils.getFilenameExtension(file.getOriginalFilename());

			String uuid = UUID.randomUUID().toString();
			String filePath = Paths.get(videoDirectory, uuid + "." + videoExtension).toString();

			Files.copy(file.getInputStream(), Paths.get(filePath));

            String thumbnailDirectory = projectDirectory +"/Thumbnail/";
			
			Path thumbnail = Paths.get(thumbnailDirectory);
			if (!Files.exists(thumbnail)) {
				Files.createDirectories(thumbnail);
			}
			String thumbnailExtension = StringUtils.getFilenameExtension(videoThumbnail.getOriginalFilename());

			String thumbnailUuid = UUID.randomUUID().toString();
			String thumbnailFilePath = Paths.get(thumbnailDirectory, thumbnailUuid + "." + thumbnailExtension).toString();

			Files.copy(file.getInputStream(), Paths.get(thumbnailFilePath));
			
			User user = userRepository.findByUsername(username.trim());

			Video saveVideo = new Video();

			saveVideo.setTitle(title);
			saveVideo.setDescription(description);
			saveVideo.setFilename(uuid + "." + videoExtension);
			saveVideo.setVideoThumbnail(thumbnailUuid+ "." + thumbnailExtension);
			saveVideo.setApprovalStatus("NEW");
			saveVideo.setCreatedBy(user.getName());
			saveVideo.setUpdatedBy(user.getName());
			saveVideo.setSize(size);
			saveVideo.setDuration(duration);
			saveVideo.setUser(user);

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

			String videoDirectory = System.getProperty("user.dir") + "/Videos/";

			logger.info("Valid token, Next go to video streaming process");

			File file = new File(videoDirectory, filename);

			StreamingResponseBody responseBody = outputStream -> {
				logger.info("Get the input stream content from video");
				try (InputStream inputStream = new FileInputStream(file)) {
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inputStream.read(buffer)) != -1) {
						outputStream.write(buffer, 0, length);
					}
				} catch (ClientAbortException e) {
					logger.warn("Client disconnected during streaming");
				} catch (IOException e) {
					logger.error("Unable to write content");
					e.printStackTrace();
				}
			};

			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.parseMediaType(getContentType(filename)));
			headers.set("Accept-Ranges", "bytes");

			logger.info("Video is streamed");
			return ResponseEntity.ok().headers(headers).body(responseBody);
		} catch (Exception e) {
			logger.error("Unable to streaming the video");
			throw new CustomStreamException("Unable to streaming the video");
		}
	}

	/**
	 * This method is used to update the approval status by fileId
	 * 
	 * @param fileId
	 * @param approvalStatus
	 * @return boolean
	 */
	public boolean updateApprovalStatusByFileId(StatusUpdateRequestPOJO statusUpdateRequestPOJO) {
		logger.info("Received the request to update the video status");
		try {
			logger.info("Get the details based on fileId");
			User user = userRepository.findByUsername(statusUpdateRequestPOJO.getUsername().trim());
			if (user.getRoles().getRoleName().equals(RoleConstant.ADMIN_ROLE)) {
				int rowsAffected = videoRepository.updateApprovalStatusByFileId(statusUpdateRequestPOJO.getFileId(),
						statusUpdateRequestPOJO.getApprovalStatus().trim(), user.getName());
				if (rowsAffected > 0) {
					logger.info("Video status is updated");
					return true;
				} else {
					logger.warn("No video found with fileId: {}", statusUpdateRequestPOJO.getFileId());
					return false;
				}
			} else {
				logger.error("Given user does not update the approval status");
				return false;
			}
		} catch (Exception e) {
			logger.error("Unable to update the video status", e);
			return false;
		}
	}

	/**
	 * This method is used to tetch the video based on user role with options and
	 * approval status
	 * 
	 * @param search
	 * @param approvalStatus
	 * @param username
	 * @return List<VideoDTO>
	 */
	public List<VideoDTO> fetchVideoByOption(String search, VideoDetailsRequestPOJO videoDetailsRequestPOJO) {
		logger.info("Received the request to fetch the video's option based");
		try {
			logger.info("Pass the username to find the details");
			User user = userRepository.findByUsername(videoDetailsRequestPOJO.getUsername().trim());
			List<VideoDTO> videoDTOs;
			if (user.getRoles().getRoleName().equals(RoleConstant.ADMIN_ROLE)) {
				videoDTOs = videoRepository.findAdminVideoByOptionANDApprovalStatus(search,
						videoDetailsRequestPOJO.getApprovalStatus().trim());
				logger.info("Fetch the video's option based on admin");
			} else {
				videoDTOs = videoRepository.findCustomerVideoByOptionAndApprovalStatus(
						videoDetailsRequestPOJO.getUsername().trim(),
						videoDetailsRequestPOJO.getApprovalStatus().trim());
				logger.info("Fetch the video's option based on customer");
			}
			logger.info("Video's are fetched in option based");
			return videoDTOs;
		} catch (Exception e) {
			logger.error("Unable to fecth the video's in option based");
			throw new CustomStreamException("Unable to fecth the video's in option based");
		}
	}

	/**
	 * This method is used to fetch the video's based on user role with approval
	 * status
	 * 
	 * @param username
	 * @param approvalStatus
	 * @return List<VideoDTO>
	 */
	public List<VideoDTO> fetchVideoDetails(VideoDetailsRequestPOJO videoDetailsRequestPOJO) {
		logger.info("Received the request to fetch the video's based the user role");
		try {
			logger.info("Pass the username to find the details");
			User user = userRepository.findByUsername(videoDetailsRequestPOJO.getUsername().trim());
			List<VideoDTO> videoDTOs;
			if (user.getRoles().getRoleName().equals(RoleConstant.ADMIN_ROLE)) {
				videoDTOs = videoRepository
						.findAdminVideoByApprovalStatus(videoDetailsRequestPOJO.getApprovalStatus().trim(),
						      videoDetailsRequestPOJO.getLimitSize() ,
						      videoDetailsRequestPOJO.getLimitSize()*(videoDetailsRequestPOJO.getPageNo()-1));
				logger.info("Fetch the video's based on admin");
			} else {
				videoDTOs = videoRepository.findCustomerVideoByApprovalStatus(
						videoDetailsRequestPOJO.getApprovalStatus().trim(),
						videoDetailsRequestPOJO.getUsername().trim());
				logger.info("Fetch the video's based on customer");
			}
			logger.info("Video's are fetched");
			return videoDTOs;
		} catch (Exception e) {
			logger.error("Unable to fecth the video details ");
			throw new CustomStreamException("Unable to fecth the video details");
		}
	}

	/**
	 * This method is used to fetch the video's without current admin or customer
	 * video
	 * 
	 * @param approvalStatus
	 * @param username
	 * @return List<VideoDTO>
	 */
	public List<VideoDTO> fetchVideoWithoutCurrentUserVideo(VideoDetailsRequestPOJO videoDetailsRequestPOJO) {
		logger.info("Received the request to fetch the video's without current admin or customer");
		List<VideoDTO> videoDTOs;
		try {
			videoDTOs = videoRepository.findVideosWithoutCurrentAdminOrCustomer(
					videoDetailsRequestPOJO.getApprovalStatus().trim(), videoDetailsRequestPOJO.getUsername().trim());
			logger.info("Video's are fetched without current user");
		} catch (Exception e) {
			logger.error("Unable to fecth the video details without current user");
			throw new CustomStreamException("Unable to fecth the video details without current user");
		}
		return videoDTOs;
	}

	/**
	 * This method is used to fetch the video's by filter option
	 * 
	 * @param filterVideoRequest
	 * @return List<VideoDTO>
	 */
	public List<VideoDTO> fetchFilterVideoDetails(FilterVideoRequest filterVideoRequest) {
	    logger.info("Received the request to fetch the videos by filter");
	    try {
	    	
	        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("FILTER_OPTION_VIDEO");

 	        query.registerStoredProcedureParameter("userId", Integer.class, ParameterMode.IN);
	        query.registerStoredProcedureParameter("roleName", String.class, ParameterMode.IN);
	        query.registerStoredProcedureParameter("approvalStatus", String.class, ParameterMode.IN);
	        query.registerStoredProcedureParameter("limitSize", Integer.class, ParameterMode.IN);
	        query.registerStoredProcedureParameter("pageNo", Integer.class, ParameterMode.IN);
	        query.registerStoredProcedureParameter("filterPojo", String.class, ParameterMode.IN);

	        String filterPojoJson = objectMapper.writeValueAsString(filterVideoRequest.getFilterPojo());
            
	        query.setParameter("userId", filterVideoRequest.getUserId());
	        query.setParameter("roleName", filterVideoRequest.getRoleName());
	        query.setParameter("approvalStatus", filterVideoRequest.getApprovalStatus());
	        query.setParameter("limitSize", filterVideoRequest.getLimitSize());
	        query.setParameter("pageNo", filterVideoRequest.getLimitSize()*(filterVideoRequest.getPageNo() -1));
	        query.setParameter("filterPojo", filterPojoJson);

	        List<Object[]> resultList = query.getResultList();
	 
	        List<VideoDTO> videoDTOs = new ArrayList<>();
	        for (Object[] result : resultList) {
	            int fileId = (int) result[0];
	            String title = String.valueOf(result[1]);
	            String description = String.valueOf(result[2]);
	            String filename = String.valueOf(result[3]);
	            float duration = (float) result[4];
	            String username = String.valueOf(result[5]);

	            VideoDTO videoDTO = new VideoDTO(fileId, title, description, filename, duration, username);
	            videoDTOs.add(videoDTO);
	        }
	   
	        return videoDTOs;

	    } catch (Exception e) {
	        e.printStackTrace();
	        logger.error("Unable to fetch the video details by filter");
	        throw new CustomStreamException("Unable to fetch the video details by filter");
	    }
	}

	
}