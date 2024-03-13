package com.tm.videostream.pojo;

public class FilterPojo {

	private String titleOperator;
	private String searchValue;
	
	private String sizeOperator;
	private float videoSize;
	private float videoFromSize;
	private float toSize;

	private String durationOperator;
	private float durationSize;
	private float fromDuration;
	private float toDuration;

	private String dateValue;
	private String dateOperator;
	private String fromDate;
	private String toDate;
	
	public OrderPojo orderClause;
	
	public String getTitleOperator() {
		return titleOperator;
	}

	public void setTitleOperator(String titleOperator) {
		this.titleOperator = titleOperator;
	}

	public String getSearchValue() {
		return searchValue;
	}

	public void setSearchValue(String searchValue) {
		this.searchValue = searchValue;
	}

	public String getSizeOperator() {
		return sizeOperator;
	}

	public void setSizeOperator(String sizeOperator) {
		this.sizeOperator = sizeOperator;
	}

	public float getVideoSize() {
		return videoSize;
	}

	public void setVideoSize(float videoSize) {
		this.videoSize = videoSize;
	}

	public float getVideoFromSize() {
		return videoFromSize;
	}

	public void setVideoFromSize(float videoFromSize) {
		this.videoFromSize = videoFromSize;
	}

	public float getToSize() {
		return toSize;
	}

	public void setToSize(float toSize) {
		this.toSize = toSize;
	}

	public String getDurationOperator() {
		return durationOperator;
	}

	public void setDurationOperator(String durationOperator) {
		this.durationOperator = durationOperator;
	}

	public float getDurationSize() {
		return durationSize;
	}

	public void setDurationSize(float durationSize) {
		this.durationSize = durationSize;
	}

	public float getFromDuration() {
		return fromDuration;
	}

	public void setFromDuration(float fromDuration) {
		this.fromDuration = fromDuration;
	}

	public float getToDuration() {
		return toDuration;
	}

	public void setToDuration(float toDuration) {
		this.toDuration = toDuration;
	}

	public String getDateValue() {
		return dateValue;
	}

	public void setDateValue(String dateValue) {
		this.dateValue = dateValue;
	}

	public String getDateOperator() {
		return dateOperator;
	}

	public void setDateOperator(String dateOperator) {
		this.dateOperator = dateOperator;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public OrderPojo getOrderClause() {
		return orderClause;
	}

	public void setOrderClause(OrderPojo orderClause) {
		this.orderClause = orderClause;
	}

}
