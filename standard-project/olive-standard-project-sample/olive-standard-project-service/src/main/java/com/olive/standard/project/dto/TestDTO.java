package com.olive.standard.project.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 14:07
 */
@Data
public class TestDTO {

    private Long id;

    private String name;

    private List<String> list;

    private Map<Integer, String> map;

    private List<TestItemDTO> items;
}
