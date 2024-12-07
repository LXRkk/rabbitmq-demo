package com.lxrkk.util;

/**
 * 睡眠工具类
 *
 * @author : LXRkk
 * @date : 2024/12/7 20:53
 */
public class SleepUtils {

    public static void sleep(long second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
