package com.olive.standard.project.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 14:22
 */
@Data
public class TestItemResp {
    private Long id;

    private List<String> details;
}
