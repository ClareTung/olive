package com.olive.skywalking.alarm.controller;

import com.alibaba.fastjson.JSON;
import com.olive.skywalking.alarm.model.AlarmMessageDTO;
import com.olive.skywalking.alarm.service.DingTalkAlarmService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 */
@Slf4j
@RestController
@RequestMapping("/skywalking")
public class AlarmController {

    @Autowired
    private DingTalkAlarmService dingTalkAlarmService;

    @PostMapping("/alarm")
    public void alarm(@RequestBody List<AlarmMessageDTO> alarmMessageDTOList) {
        log.info("收到告警信息: {}", JSON.toJSONString(alarmMessageDTOList));
        if (null != alarmMessageDTOList) {
            alarmMessageDTOList.forEach(e -> dingTalkAlarmService.sendMessage(MessageFormat.format("-----来自SkyWalking的告警:common-bff-----\n【名称】: {0}\n【消息】: {1}\n", e.getName(), e.getAlarmMessage())));
        }
    }
}
