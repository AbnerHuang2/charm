package com.abner.charm.webSocket.entity;

import lombok.Data;

/**
 * @author: huanghousheng
 * @date: 2021/4/19
 * @description:
 */
@Data
public class MsgEntity {
    private Long from;
    private Long to;
    private Long createTime;
    private Integer msgType;
    private Integer chatType;
    private String content;
    /**
     *  0 : 无
     */
    private Long groupId;
}
