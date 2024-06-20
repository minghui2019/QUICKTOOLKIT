package com.eplugger.uuid.utils;

import java.util.List;

import com.eplugger.uuid.entity.UUID;
import com.google.common.collect.Lists;

import static java.util.UUID.randomUUID;

public class UUIDUtils {
    private UUIDUtils() {
    }

    /**
     * 生成UUID
     * @param count
     * @return
     */
    public static List<UUID> generateUUID(int count) {
        List<UUID> uuidList = Lists.newArrayList();
        for (int i = 0; i < count; i++) {
            uuidList.add(new UUID(i + 1, randomUUID().toString().replace("-", "")));
        }
        return uuidList;
    }
}
