package com.tm.videostream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tm.videostream.entity.Video;

public interface VideoRepository extends JpaRepository<Video, Integer> {

}
