package com.olive.standard.project.dto;

import lombok.Data;

import java.util.List;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 14:10
 */
@Data
public class TestItemDTO {
    private Long id;

    private List<String> details;
}
