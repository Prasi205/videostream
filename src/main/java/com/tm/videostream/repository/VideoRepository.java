package com.tm.videostream.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE Video SET approvalStatus = :approvalStatus, updatedBy = :name, "
			+ "updatedAt = CURRENT_TIMESTAMP WHERE fileId = :fileId")
	int updateApprovalStatusByFileId(int fileId, String approvalStatus, String name);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video "
			+ "INNER JOIN User user ON video.user.userId = user.userId "
			+ "WHERE video.approvalStatus = :approvalStatus ORDER BY video.fileId DESC limit limitSize offset pageNo")
	List<VideoDTO> findAdminVideoByApprovalStatus(String approvalStatus,int limitSize, int pageNo);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video "
			+ "WHERE video.approvalStatus = :approvalStatus AND video.user.username = :username ORDER BY video.fileId DESC")
	List<VideoDTO> findCustomerVideoByApprovalStatus(String approvalStatus, String username);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video "
			+ "INNER JOIN User user ON video.user.userId = user.userId "
			+ "WHERE (video.title = :search OR video.description = :search )"
			+ "AND video.approvalStatus = :approvalStatus ORDER BY video.fileId DESC")
	List<VideoDTO> findAdminVideoByOptionANDApprovalStatus(String search, String approvalStatus);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video WHERE video.user.username = :username "
			+ "AND (video.title = :search OR video.description = :search) "
			+ "AND video.approvalStatus = :approvalStatus ORDER BY video.fileId DESC")
	List<VideoDTO> findCustomerVideoByOptionAndApprovalStatus(String username, String approvalStatus);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video " 
			+ "WHERE video.approvalStatus = :approvalStatus ORDER BY video.fileId DESC")
	List<VideoDTO> findVideoByApprovalStatus(String approvalStatus);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.filename, video.duration, user.username) FROM Video video "
			+ "INNER JOIN User user ON video.user.userId = user.userId "
			+ "WHERE video.approvalStatus = :approvalStatus and user.username <>:username ORDER BY video.fileId DESC")
	List<VideoDTO> findVideosWithoutCurrentAdminOrCustomer(String approvalStatus, String username);

}
