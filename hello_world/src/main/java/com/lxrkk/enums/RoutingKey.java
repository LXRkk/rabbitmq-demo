package com.lxrkk.enums;

/**
 * routingKey 枚举类
 *
 * @author : LXRkk
 * @date : 2024/12/10 17:29
 */
public enum RoutingKey {
    ERROR("错误日志……"),
    INFO("正常日志……"),
    WARNING("警告日志……");

    private String value;

    RoutingKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
