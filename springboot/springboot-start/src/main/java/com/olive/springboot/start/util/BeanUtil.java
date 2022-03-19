package com.olive.springboot.start.util;

import com.olive.springboot.start.annotation.NeedSetValue;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dongtangqiang
 */
@Component
public class BeanUtil implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    /**
     * 遍历集合设置集合中对象的属性值，利用反射机制
     *
     * @param coll
     * @throws Exception
     */
    public void setFiledValueForColl(Collection coll) throws Exception {
        // [{"id":1,"customerId":1000,"customerName":null}]
        // 获取注解 -- method名字
        // 反射调用method.invoke() -- 获取到User对象 -- 获取name属性 -- 设置到结果集

        // Order对象
        Class<?> clazz = coll.iterator().next().getClass();
        // Order对象的属性
        Field[] fields = clazz.getDeclaredFields();

        // 缓存
        Map<String, Object> cache = new HashMap<>();

        for (Field needField : fields) {
            // 获取注解
            NeedSetValue nsv = needField.getAnnotation(NeedSetValue.class);
            if (nsv == null) {
                continue;
            }

            // 设置该属性可见
            needField.setAccessible(true);

            Object bean = this.applicationContext.getBean(nsv.beanClass());

            // public com.olive.springboot.start.model.User com.olive.springboot.start.service.UserServiceImpl.findById(java.lang.Long)
            Method method = nsv.beanClass().getMethod(nsv.method(), clazz.getDeclaredField(nsv.param()).getType());

            // class com.olive.springboot.start.model.Order
            Field paramField = clazz.getDeclaredField(nsv.param());
            paramField.setAccessible(true);

            Field targetField = null;
            boolean needInnerField = StringUtils.isNotEmpty(nsv.targetField());
            String keyPrefix = nsv.beanClass() + "-" + nsv.method() + "-" + nsv.targetField() + "-";

            // 循环结果集
            for (Object obj : coll) {
                // 入参的具体值
                Object paramValue = paramField.get(obj);
                if (paramValue == null) {
                    continue;
                }

                Object value = null;
                String key = keyPrefix + paramValue;

                if (cache.containsKey(key)) {
                    value = cache.get(key);
                } else {
                    // value是一个User对象
                    value = method.invoke(bean, paramValue);
                    if (needInnerField) {
                        if (value != null) {
                            if (targetField == null) {
                                targetField = value.getClass().getDeclaredField(nsv.targetField());
                                targetField.setAccessible(true);
                            }
                            // User name的具体值
                            value = targetField.get(value);
                        }
                    }

                    cache.put(key, value);
                }

                // 设置值
                needField.set(obj, value);
            }
        }

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
