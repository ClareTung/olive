package com.olive.standard.project.vo.resp;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 14:21
 */
@Data
public class TestResp {
    private Long id;

    private String name;

    private List<String> list;

    private Map<Integer, String> map;

    private List<TestItemResp> items;
}
