package com.eplugger.xml.dom4j.entity;

import lombok.Data;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import java.util.List;

/**
 * 巡检站
 */
@Data
@Dom4JTag
public class TourStation {
    @Dom4JField
    private String stationName;

    @Dom4JField
    private int location;

    @Dom4JField
    private int endLocation;

    @Dom4JField
    private int speed;

    @Dom4JField(type = Dom4JFieldType.TAG)
    private List<TourAction> tourAction;
}
