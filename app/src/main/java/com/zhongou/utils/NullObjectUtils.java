package com.zhongou.utils;

/**
 * NullObject模式，本项目没实践使用，写出来练着玩，具体使用用三目运算符即可
 * Created by sjy on 2017/1/17.
 */

public class NullObjectUtils {
    private NullObjectUtils(){}
    private static class NullObjectContainer{
        private static NullObjectUtils instance = new NullObjectUtils();
    }
    public static NullObjectUtils getInstance(){
        return NullObjectContainer.instance;
    }
    public static String NullMessage(){
        return "空值null";
    }
}
