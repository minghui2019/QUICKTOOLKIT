package com.eplugger.uuid.entity;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import lombok.Data;
import top.tobak.xml.dom4j.annotation.Dom4JField;
import top.tobak.xml.dom4j.annotation.Dom4JFieldType;
import top.tobak.xml.dom4j.annotation.Dom4JTag;

import static com.google.common.base.Preconditions.checkNotNull;

@Data
@Dom4JTag("uuids")
public class UUIDS implements Collection<UUID> {
	@Dom4JField(type = Dom4JFieldType.TAG)
	private List<UUID> uuidList;

	public UUIDS() {
		uuidList = Lists.newArrayList();
	}

	@Override
	public int size() {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		return uuidList.size();
	}

	@Override
	public boolean addAll(Collection<? extends UUID> c) {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		return uuidList.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException("removeAll is not supported!");
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException("retainAll is not supported!");
	}

	@Override
	public void clear() {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		uuidList.clear();
	}

	public boolean isEmpty() {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		return uuidList.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		throw new UnsupportedOperationException("contains is not supported!");
	}

	@Override
	public Iterator<UUID> iterator() {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		return uuidList.iterator();
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException("toArray is not supported!");
	}

	@Override
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException("toArray is not supported!");
	}

	@Override
	public boolean add(UUID uuid) {
		checkNotNull(uuidList, "com.eplugger.uuid.entity.UUIDS.uuidList is null!");
		return uuidList.add(uuid);
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException("remove is not supported!");
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		throw new UnsupportedOperationException("containsAll is not supported!");
	}
}
