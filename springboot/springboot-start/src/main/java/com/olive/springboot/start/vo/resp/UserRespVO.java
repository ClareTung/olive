package com.olive.springboot.start.vo.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @desc: 用户响应视图
 * @classname: UserVO
 * @author: dongtangqiang
 * @date: 2020-12-31
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserRespVO {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String userNick;

    /**
     * 用户爱好
     */
    private List<String> hobby;

    /**
     * 生日
     */
    // @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;
}
