package com.eplugger.uuid.entity;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collection;
import java.util.List;

import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;
import com.google.common.collect.Lists;

import lombok.Data;

@Data
@Dom4JTag("uuids")
public class Uuids {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<Uuid> uuidList = Lists.newArrayList();
	
	public int size() {
		checkNotNull(this.uuidList, "com.eplugger.uuid.entity.Uuids.uuidList is null!");
		return this.uuidList.size();
	}
	
	public boolean addAll(Collection<? extends Uuid> c) {
		checkNotNull(this.uuidList, "com.eplugger.uuid.entity.Uuids.uuidList is null!");
		return this.uuidList.addAll(c);
	}

	public boolean isEmpty() {
		checkNotNull(this.uuidList, "com.eplugger.uuid.entity.Uuids.uuidList is null!");
		return this.uuidList.isEmpty();
	}
}
