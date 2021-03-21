package com.olive.standard.project.service.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.olive.standard.project.dto.TestDTO;
import com.olive.standard.project.dto.TestItemDTO;
import com.olive.standard.project.service.TestService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @program: olive
 * @author: dtq
 * @create: 2021/3/1 14:12
 */
@Service
public class TestServiceImpl implements TestService {

    @Override
    public TestDTO getTestDTO() {
        TestDTO testDTO = new TestDTO();
        testDTO.setId(1001L);
        testDTO.setName("1001");
        testDTO.setList(Lists.newArrayList("a", "b", "c"));
        Map<Integer, String> map = Maps.newHashMap();
        map.put(1, "11");
        map.put(2, "22");
        testDTO.setMap(map);

        List<TestItemDTO> items = Lists.newArrayList();
        TestItemDTO item1 = new TestItemDTO();
        item1.setId(1001001L);
        item1.setDetails(Lists.newArrayList("d1", "d2", "d3", "d4"));
        items.add(item1);
        TestItemDTO item2 = new TestItemDTO();
        item2.setId(1001002L);
        item2.setDetails(Lists.newArrayList("e1", "e2", "e3", "e4"));
        items.add(item2);

        testDTO.setItems(items);

        return testDTO;
    }
}
