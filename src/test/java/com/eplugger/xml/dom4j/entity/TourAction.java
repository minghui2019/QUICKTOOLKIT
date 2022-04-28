package com.eplugger.xml.dom4j.entity;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data


// @Dom4JTag 建立TourAction和XMLObject映射关系
@Dom4JTag
@NoArgsConstructor
@AllArgsConstructor
public class TourAction {

    @Dom4JField
    // @Dom4JField(name = "mountDvsType", type = FieldType.ATTRIBUTE)
    // 以上两种注解方式完全一致
    private int mountDvsType;

    @Dom4JField
    private String actionName;

    @Dom4JField
    private int detectedDvsType;

    /*
     * <TourAction mountDvsType="1" actionName="默认动作名称" detectedDvsType="0">
     *     <SensorInfo time="1599058642"/>
     * </TourAction>
     * 通过 path&name 读取子孙节点属性(或标签体)的值
     * path + name => TourAction.SensorInfo[time]
     */
    @Dom4JField(name = "time", type = Dom4JFieldType.ATTRIBUTE, path = "SensorInfo")
    private long sensorInfoTime;

    /*
     * <SnapshotPosition arm1="0" arm2="90" arm3="0" arm4="0" arm5="0" arm6="0" zoom="1" thermal="1">
     *     <SnapshotSample image="仪表_20200902_145725.jpg" analysis="" time="1599058645"/>
     * </SnapshotPosition>
     * 指明 snapshotPosition 数据来源是直接子标签, 具体映射方案由 SnapshotPosition 类的 @Dom4JTag & @Dom4JField 决定
     */
    @Dom4JField(type = Dom4JFieldType.TAG)
    private SnapshotPosition snapshotPosition;
}
