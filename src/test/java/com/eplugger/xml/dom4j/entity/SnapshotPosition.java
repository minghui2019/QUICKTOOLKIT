package com.eplugger.xml.dom4j.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

/**
 * 快照位置
 */
@Data
@Dom4JTag
@NoArgsConstructor
@AllArgsConstructor
public class SnapshotPosition {

    @Dom4JField
    private double arm1;

    @Dom4JField
    private double arm2;

    @Dom4JField
    private double arm3;

    @Dom4JField
    private double arm4;

    @Dom4JField
    private double arm5;

    @Dom4JField
    private double arm6;

    @Dom4JField
    private double zoom;

    @Dom4JField
    private double thermal;

    @Dom4JField(type = Dom4JFieldType.TAG)
    private SnapshotSample snapshotSample;

}
