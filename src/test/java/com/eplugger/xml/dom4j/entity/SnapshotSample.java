package com.eplugger.xml.dom4j.entity;

import lombok.Data;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

/**
 * 快照样本
 */
@Data
@Dom4JTag
public class SnapshotSample {

    @Dom4JField
    private String image;
    @Dom4JField
    private String analysis;
    @Dom4JField
    private String time;

}
