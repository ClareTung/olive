package com.olive.springboot.start.vo.resp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HomeWorkVO {

    /**
     * 作业id
     */
    private Long id;

    /**
     * 作业名称
     */
    @JsonProperty("home_work_name")
    private String homeWorkName;

    /**
     * 作业内容
     */
    @JsonProperty("home_work_content")
    private String homeWorkContent;
}
