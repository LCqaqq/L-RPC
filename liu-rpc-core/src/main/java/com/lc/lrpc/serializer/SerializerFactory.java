package com.lc.lrpc.serializer;

import com.lc.lrpc.spi.SpiLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * 序列化器工厂
 */
public class SerializerFactory {
//    /**
//     * 序列化映射（用于实现单例）
//     */
//    private static final Map<String,Serializer> KEY_SERIALIZER_MAP = new HashMap<String,Serializer>(){
//        {
//            put(SerializerKeys.JDK, new JdkSerializer());
//            put(SerializerKeys.JSON,new JsonSerializer());
//        }
//    };
//    private static final Serializer DEFAULT_SERIALIZER =KEY_SERIALIZER_MAP.get("jdk");
//
//    public static Serializer getInstance(String key) {
//        return KEY_SERIALIZER_MAP.getOrDefault(key,DEFAULT_SERIALIZER);
//    }

    static {
        SpiLoader.load(Serializer.class);
   }
    /**
     * 默认序列化器
     */
    private static final Serializer DEFAULT_SERIALIZER = new JdkSerializer();

        public static Serializer getInstance(String key) {
        return SpiLoader.getInstance(Serializer.class,key);
    }



}
