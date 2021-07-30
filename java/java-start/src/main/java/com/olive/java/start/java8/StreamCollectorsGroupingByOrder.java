package com.olive.java.start.java8;

import com.google.common.collect.Lists;
import com.olive.java.start.java8.entity.City;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description: Stream将List通过Collectors.groupingBy转换成Map后排序被打乱的问题
 * @program: dtq
 * @author: dtq
 * @create: 2021/7/30 18:35
 */
public class StreamCollectorsGroupingByOrder {
    public static void main(String[] args) {

        City c1 = new City("安徽省", "安庆市");
        City c2 = new City("安徽省", "芜湖市");
        City c3 = new City("上海市", "青浦区");
        City c4 = new City("浙江省", "杭州市");
        City c5 = new City("上海市", "徐汇区");

        List<City> cityList = Lists.newArrayListWithCapacity(5);
        cityList.add(c1);
        cityList.add(c2);
        cityList.add(c3);
        cityList.add(c4);
        cityList.add(c5);

        System.out.println("- - - List - - -");
        cityList.forEach(city -> System.out.println(city.getProvince() + "- - -" + city.getCity()));

        // Map<String, List<City>> cityMap = cityList.stream().collect(Collectors.groupingBy(City::getProvince));
        /*
         * groupingBy(classifier, HashMap::new, downstream);
         * HashMap的底层是哈希表，是无序的，存取数据不同
         */

        Map<String, List<City>> cityMap = cityList.stream().collect(Collectors.groupingBy(City::getProvince, LinkedHashMap::new, Collectors.toList()));

        System.out.println("- - - 分组转换成Map后 - - -");
        cityMap.forEach((key, value) -> value.forEach(city -> System.out.println(city.getProvince() + "- - -" + city.getCity())));
    }
}
