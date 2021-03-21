package com.olive.skywalking.alarm.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @description:
 * @program: olive
 * @author: dtq
 */
@Data
public class AlarmMessageDTO implements Serializable {
    private int scopeId;

    private String scope;

    /**
     * Target scope entity name
     */
    private String name;

    private String id0;

    private String id1;

    private String ruleName;

    /**
     * Alarm text message
     */
    private String alarmMessage;

    /**
     * Alarm time measured in milliseconds
     */
    private long startTime;
}
