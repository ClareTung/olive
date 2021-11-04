package com.olive.fpc.mq.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/11/3 16:01
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse {

    private int status = 200;
    private String message;
}
