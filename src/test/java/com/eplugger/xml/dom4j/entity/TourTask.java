package com.eplugger.xml.dom4j.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

@Data
@NoArgsConstructor
@Dom4JTag("TourTask")
public class TourTask {

    @Dom4JField(name = "taskName", type = Dom4JFieldType.ATTRIBUTE)
    private String taskName;

    @Dom4JField
    private int tourType;

    @Dom4JField
    private String robotID;
}
