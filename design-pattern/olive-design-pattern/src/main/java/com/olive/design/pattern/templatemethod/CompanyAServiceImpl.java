package com.olive.design.pattern.templatemethod;

import org.springframework.stereotype.Service;

/**
 * @author dongtangqiang
 */
@Service
public class CompanyAServiceImpl extends AbstractMerchantService{
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
        return true;
    }
}
