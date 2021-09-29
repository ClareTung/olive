package com.olive.ads.start.consistenthash;

/**
 * @description: 一致性Hash算法测试
 * @program: olive
 * @author: dtq
 * @create: 2021/9/29 14:23
 */
public class ConsistentHashingTest {

    public static void main(String[] args) {

        String[] servers = new String[]{"192.168.0.0:111", "192.168.0.1:111", "192.168.0.2:111",
                "192.168.0.3:111", "192.168.0.4:111"};
        ConsistentHashing consistentHashing = new ConsistentHashingImpl(5, servers);
        String[] data = new String[]{"127.0.0.1:1111", "221.226.0.1:2222", "10.211.0.1:3333"};

        for (String str : data) {
            consistentHashing.putData(str);
        }
        consistentHashing.removeNode("192.168.0.1:111");
        consistentHashing.addNode("192.168.0.5:111");
        consistentHashing.putData("10.185.0.1:4444");
        consistentHashing.printAllData();
    }
}
