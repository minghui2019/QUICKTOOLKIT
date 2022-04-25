package com.eplugger.uuid.entity;

import java.util.List;

import com.eplugger.xml.dom4j.annotation.Dom4JField;
import com.eplugger.xml.dom4j.annotation.Dom4JFieldType;
import com.eplugger.xml.dom4j.annotation.Dom4JTag;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
@Dom4JTag("uuids")
public class Uuids {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<Uuid> uuidList = Lists.newArrayList();
}
