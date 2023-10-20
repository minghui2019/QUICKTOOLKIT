package com.eplugger.xml.dom4j.entity;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Dom4JTag
public class TourStatus {
	@Dom4JField
	private String taskName;
	@Dom4JField
	private String tourType;
	@Dom4JField
	private String isAnalysis;
	@Dom4JField
	private String robotID;
	@Dom4JField
	private String robotName;
	@Dom4JField
	private String beginTime;
	@Dom4JField
	private String endTime;
	@Dom4JField(name = "DetectedNum")
	private String detectedNum;
	@Dom4JField(name = "TotalStationNum")
	private String totalStationNum;
	@Dom4JField
	private String status;
	@Dom4JField
	private String statusParam;
	@Dom4JField
	private String errCode;
}
