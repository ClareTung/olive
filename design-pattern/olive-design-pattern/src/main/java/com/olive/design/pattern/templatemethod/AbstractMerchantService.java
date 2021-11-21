package com.olive.design.pattern.templatemethod;

/**
 * 模板方法：抽象类类定义骨架流程，差异化由子类实现
 *
 * @author dongtangqiang
 */
public abstract class AbstractMerchantService {

    // 查询商户消息
    abstract void queryMerchantInfo();

    // 加签
    abstract void signature();

    // http请求
    abstract void httpRequest();

    // 验签
    abstract void verifySignature();

    void handlerTemplate() {
        queryMerchantInfo();
        signature();
        httpRequest();
        verifySignature();
    }

    /**
     * 差异化子类实现，http是否走代理
     *
     * @return
     */
    abstract boolean isRequestByProxy();

}
