package com.olive.springframwork.beans.factory.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.olive.springframwork.beans.BeansException;
import com.olive.springframwork.beans.factory.DisposableBean;
import com.olive.springframwork.beans.factory.config.SingletonBeanRegistry;

/**
 * 类DefaultSingletonBeanRegistry的实现描述：单例注册接口实现
 *
 * @author dongtangqiang 2022/5/26 22:00
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * Internal marker for a null singleton object:
     * used as marker value for concurrent Maps (which don't support null values).
     */
    protected static final Object NULL_OBJECT = new Object();

    private Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeans = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

//    protected void addSingleton(String beanName, Object singletonObject){
//        singletonObjects.put(beanName, singletonObject);
//    }

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeans.put(beanName, bean);
    }

    public void destroySingletons() {
        Set<String> keySet = this.disposableBeans.keySet();
        Object[] disposableBeanNames = keySet.toArray();

        for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
            Object beanName = disposableBeanNames[i];
            DisposableBean disposableBean = disposableBeans.remove(beanName);
            try {
                disposableBean.destroy();
            } catch (Exception e) {
                throw new BeansException("Destroy method on bean with name '" + beanName + "' threw an exception", e);
            }
        }
    }
}
