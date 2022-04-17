package com.eplugger.xml.dom4j.entity;

import lombok.Data;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

@Data
@Dom4JTag
public class TourLine {

    @Dom4JField
    private String lineName;

}