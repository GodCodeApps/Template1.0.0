package com.meishe.common.utils;

import java.util.Collection;

public class CollectionUtils {
    public static <T extends Collection> boolean isEmpty(T t) {
        return t == null || t.isEmpty();
    }

    public static <T extends Collection> boolean isIndexAvailable(int i, T t) {
        return t != null && i >= 0 && i < t.size();
    }
}
