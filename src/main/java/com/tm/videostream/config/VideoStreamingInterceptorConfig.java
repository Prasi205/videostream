package com.tm.videostream.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tm.videostream.interceptor.StreamingInterceptor;

@Configuration
public class VideoStreamingInterceptorConfig implements WebMvcConfigurer{
	
	@Autowired
	private StreamingInterceptor streamingInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
	    registry.addInterceptor(streamingInterceptor)
	            .addPathPatterns("/**") 
	            .excludePathPatterns("/auth/*");
	}

}
