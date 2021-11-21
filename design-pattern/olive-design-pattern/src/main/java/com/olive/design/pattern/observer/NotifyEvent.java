package com.olive.design.pattern.observer;

import lombok.Data;

/**
 * 通知事件类
 *
 * @author dongtangqiang
 */
@Data
public class NotifyEvent {

    private String mobileNo;

    private String emailNo;

    private String imNo;

    public NotifyEvent(String mobileNo, String emailNo, String imNo) {
        this.mobileNo = mobileNo;
        this.emailNo = emailNo;
        this.imNo = imNo;
    }
}
