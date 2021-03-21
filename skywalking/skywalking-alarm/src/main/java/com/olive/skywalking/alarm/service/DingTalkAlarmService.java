package com.olive.skywalking.alarm.service;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.taobao.api.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @description:
 * @program: olive
 * @author: dtq
 */
@Slf4j
@Service
public class DingTalkAlarmService {
    @Value("${dingtalk.webhook}")
    private String webhook;
    @Value("${dingtalk.secret}")
    private String secret;

    public void sendMessage(String content) {
        try {
            Long timestamp = System.currentTimeMillis();
            String stringToSign = timestamp + "\n" + secret;
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
            byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
            String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)), "UTF-8");

            String serverUrl = webhook + "&timestamp=" + timestamp + "&sign=" + sign;
            DingTalkClient client = new DefaultDingTalkClient(serverUrl);
            OapiRobotSendRequest request = new OapiRobotSendRequest();
            request.setMsgtype("text");
            OapiRobotSendRequest.Text text = new OapiRobotSendRequest.Text();
            text.setContent(content);
            request.setText(text);

            client.execute(request);
        } catch (ApiException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
    }
}
