package com.tm.videostream.pojo.request;

import com.tm.videostream.pojo.FilterPojo;

public class FilterVideoRequest {

	private String uniqueId;
	private String approvalStatus;
	private int limitSize;
	private int pageNo;
	private FilterPojo filterPojo;

	public String getUniqueId() {
		return uniqueId;
	}

	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public int getLimitSize() {
		return limitSize;
	}

	public void setLimitSize(int limitSize) {
		this.limitSize = limitSize;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public FilterPojo getFilterPojo() {
		return filterPojo;
	}

	public void setFilterPojo(FilterPojo filterPojo) {
		this.filterPojo = filterPojo;
	}

}
