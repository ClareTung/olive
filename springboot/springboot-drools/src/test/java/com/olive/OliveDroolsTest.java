package com.olive;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.olive.drools.OliveDroolsApplication;
import com.olive.drools.bean.Cat;
import com.olive.drools.bean.People;

/**
 * 类OliveDroolsTest的实现描述：OliveDroolsTest
 *
 * @author dongtangqiang 2022/2/10 15:19
 */
@SpringBootTest(classes = {OliveDroolsApplication.class})
@RunWith(SpringRunner.class)
public class OliveDroolsTest {

    @Autowired
    private KieSession session;

    @Test
    public void testPeopleRule() {
        People people = new People();
        people.setName("强");
        people.setSex(1);
        people.setDrlType("people");

        // 插入
        session.insert(people);
        // 执行规则
        session.fireAllRules();
    }

    @Test
    public void testCatRule() {
        Cat cat = new Cat();
        cat.setName("金");
        cat.setSex(1);

        //插入
        session.insert(cat);
        //执行规则
        session.fireAllRules();
    }


    @AfterEach
    public void runDispose() {
        //释放资源
        session.dispose();
    }
}
