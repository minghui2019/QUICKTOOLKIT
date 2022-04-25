package com.eplugger.uuid.utils;

import java.util.UUID;

public class UUIDUtils {

	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String[] getUuids(int count) {
		String[] uuids = new String[count];
		for (int i = 0; i < count; i++) {
			uuids[i] = getUuid();
		}
		return uuids;
	}
}
