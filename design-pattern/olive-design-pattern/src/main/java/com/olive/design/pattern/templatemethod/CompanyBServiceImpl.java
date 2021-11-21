package com.olive.design.pattern.templatemethod;

import org.springframework.stereotype.Service;

/**
 * @author dongtangqiang
 */
@Service
public class CompanyBServiceImpl extends AbstractMerchantService {
    @Override
    void queryMerchantInfo() {

    }

    @Override
    void signature() {

    }

    @Override
    void httpRequest() {

    }

    @Override
    void verifySignature() {

    }

    @Override
    boolean isRequestByProxy() {
        // 不走代理
        return false;
    }
}
