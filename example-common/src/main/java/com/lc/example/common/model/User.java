package com.lc.example.common.model;

import lombok.Data;

import java.io.Serializable;
/**
 * 用户
 */
@Data
public class  User implements Serializable {
    private String name;

    public String getName(){
        return name;
    }

    public void SetName(String name){
        this.name = name;
    }
}
