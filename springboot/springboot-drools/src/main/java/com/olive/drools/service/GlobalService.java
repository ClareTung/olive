package com.olive.drools.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.olive.drools.bean.People;

/**
 * 类GlobalService的实现描述：GlobalService
 *
 * @author dongtangqiang 2022/2/15 16:37
 */
@Service
public class GlobalService {

    public static List<People> getPeoples() {
        List<People> peoples = new ArrayList<>();
        peoples.add(new People(1, "A", "global"));
        peoples.add(new People(2, "B", "global"));
        peoples.add(new People(3, "C", "global"));
        peoples.add(new People(4, "D", "global"));
        peoples.add(new People(5, "E", "global"));
        return peoples;
    }
}
