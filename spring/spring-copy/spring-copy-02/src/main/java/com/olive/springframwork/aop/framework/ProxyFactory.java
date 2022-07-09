package com.olive.springframwork.aop.framework;

import com.olive.springframwork.aop.AdvisedSupport;

/**
 * 类ProxyFactory的实现描述：代理工厂
 *
 * @author dongtangqiang 2022/7/9 17:58
 */
public class ProxyFactory {

    private AdvisedSupport advisedSupport;

    public ProxyFactory(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }

    private AopProxy createAopProxy() {
        if (advisedSupport.isProxyTargetClass()) {
            return new Cglib2AopProxy(advisedSupport);
        }

        return new JdkDynamicAopProxy(advisedSupport);
    }

}
