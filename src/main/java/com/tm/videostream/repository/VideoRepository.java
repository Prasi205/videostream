package com.tm.videostream.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.tm.videostream.dto.VideoDTO;
import com.tm.videostream.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {

	@Transactional
	@Modifying
	@Query("UPDATE Video SET approvalStatus = :approvalStatus, updatedBy = :name, "
			+ "updatedAt = CURRENT_TIMESTAMP WHERE fileId = :fileId")
	int updateApprovalStatusByFileId(int fileId, String approvalStatus, String name);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.videoThumbnail, video.filename, user.username) FROM Video video "
			+ "INNER JOIN User user ON video.user.userId = user.userId "
			+ "WHERE video.approvalStatus = :approvalStatus ORDER BY video.updatedAt DESC")
	List<VideoDTO> findAdminVideoByApprovalStatus(String approvalStatus,Pageable pageable);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.videoThumbnail, video.filename, user.username) FROM Video video "
			+ "WHERE video.approvalStatus = :approvalStatus AND video.user.username = :username "
			+ "ORDER BY video.updatedAt DESC")
	List<VideoDTO> findCustomerVideoByApprovalStatus(String approvalStatus, String username,Pageable pageable);

	@Query("SELECT new com.tm.videostream.dto.VideoDTO(video.fileId, video.title, video.description, "
			+ "video.videoThumbnail, video.filename, user.username) FROM Video video "
			+ "INNER JOIN User user ON video.user.userId = user.userId "
			+ "WHERE video.approvalStatus = :approvalStatus and user.username <>:username "
			+ "ORDER BY video.fileId DESC")
	List<VideoDTO> findVideosWithoutCurrentAdminOrCustomer(String approvalStatus, String username);

}
