package com.lc.lrpc.protocol;

import cn.hutool.core.util.ObjectUtil;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
@Getter
public enum ProtocolMessageSerializerEnum {
    JOK(0,"jdk"),
    JSON(1,"json");

    private final int key;
    private final String value;

    ProtocolMessageSerializerEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
    public static List<String> getValues(){
        return Arrays.stream(values()).map(item -> item.value).collect(Collectors.toList());
    }

    public static ProtocolMessageSerializerEnum getEnumByValue(int key){
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()){
            if (anEnum.key == key){
                return anEnum;
            }
        }
        return null;
    }

    public static ProtocolMessageSerializerEnum getEnumByValue(String value){
        if (ObjectUtil.isEmpty(value)){
            return null;
        }
        for (ProtocolMessageSerializerEnum anEnum : ProtocolMessageSerializerEnum.values()){
            if (anEnum.value.equals(value)){
                return anEnum;
            }
        }
        return null;
    }
}