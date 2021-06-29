package com.travel.utils;

import java.util.UUID;

/**
 * UUID工具类
 */
public class UuidUtils {

    /**
     * 生成带有-的UUID字符串
     * @return 带有-的UUID字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

    /**
     * 生成不带有-的UUID字符串
     * @return 不带有-的UUID字符串
     */
    public static String simpleUuid() {
        return uuid().replaceAll("-", "");
    }

}