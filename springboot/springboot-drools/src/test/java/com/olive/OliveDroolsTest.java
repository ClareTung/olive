package com.olive;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.runner.RunWith;
import org.kie.api.runtime.KieSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.olive.drools.OliveDroolsApplication;
import com.olive.drools.bean.Animal;
import com.olive.drools.bean.Cat;
import com.olive.drools.bean.People;
import com.olive.drools.bean.Sensor;

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

    @Test
    public void testFrom(){
        People p1 = new People(1, "A", "from");
        People p2 = new People(0, "B", "from");
        People p3 = new People(3, "C", "from");

        Animal animal = new Animal();
        animal.setPeoples(new ArrayList<>());
        animal.getPeoples().add(p1);
        animal.getPeoples().add(p2);
        animal.getPeoples().add(p3);

        session.insert(animal);
        session.fireAllRules();
    }

    @Test
    public void testCollect(){
        session.insert(new People(1, "A", "collect"));
        session.insert(new People(0, "B", "collect"));
        session.insert(new People(3, "C", "collect"));
        session.insert(new People(1, "D", "collect"));

        // 执行规则
        session.fireAllRules();
    }

    @Test
    public void testAccumulate(){
        session.insert(new Sensor("春", 4.5));
        session.insert(new Sensor("夏", 5.5));
        session.insert(new Sensor("秋", 4.5));
        session.insert(new Sensor("冬", 5.5));

        session.fireAllRules();
    }

    @Test
    public void testDiyaccumulate() {
        session.insert(new People(1, "A", 26, "diyaccumulate"));
        session.insert(new People(0, "B", 18, "diyaccumulate"));
        session.insert(new People(3, "C", 12, "diyaccumulate"));

        //执行规则
        session.fireAllRules();
    }

    @AfterEach
    public void runDispose() {
        //释放资源
        session.dispose();
    }
}
