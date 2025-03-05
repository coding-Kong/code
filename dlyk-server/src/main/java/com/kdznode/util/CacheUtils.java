package com.kdznode.util;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author kdz
 * @create 2025-03-04-11:31
 */
public class CacheUtils {
    public static <T> T getCacheData(Supplier<T> cacheSelector, Supplier<T> dbSelector, Consumer<T> cacheSave) {
        T t = cacheSelector.get();
        if (Objects.isNull(t)) {
            T db = dbSelector.get();
            if (!Objects.isNull(db)) {
                cacheSave.accept(db);
            }
            return db;
        } else {
            return t;
        }
    }
}
