package com.zhongou.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 周 枚举类型 日程安排控件使用
 * Created by yang.dong on 2015/10/23.
 */
public enum DateEnum {
    SUN("日" , 7),MON("一" , 1),TUE("二" , 2),WED("三" , 3), THU("四", 4),FRI("五", 5),SAT("六", 6);
    private String value;
    private int index;
    private DateEnum(String value, int index){
        this.value = value;
        this.index = index;
    }


    private static final Map<Integer, DateEnum> stringToCommand = new HashMap<Integer, DateEnum>();
    static{
        for(DateEnum item : values()){
            stringToCommand.put(item.getIndex(), item);
        }
    }

    public static DateEnum fromInteger(Integer commandStr){
        return stringToCommand.get(commandStr);
    }



    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
