package com.eplugger.xml.dom4j.entity;

import java.util.List;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Dom4JTag
public class TourReport {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private TourTask tourTask;
	@Dom4JField(type = Dom4JFieldType.TAG)
	private TourLine tourLine;
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<TourStation> tourStationList;
	@Dom4JField(type = Dom4JFieldType.TAG)
	private TourStatus tourStatus;
	
}
