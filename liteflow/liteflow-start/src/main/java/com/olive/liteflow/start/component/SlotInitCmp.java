package com.olive.liteflow.start.component;

import com.olive.liteflow.start.bean.PriceCalcReqVO;
import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

/**
 * Slot初始化组件
 */
@Component("slotInitCmp")
public class SlotInitCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        //把主要参数冗余到slot里
        PriceCalcReqVO req = this.getSlot().getRequestData();
        PriceSlot priceSlot = this.getSlot();
        priceSlot.setOrderNo(req.getOrderNo());
        priceSlot.setOversea(req.isOversea());
        priceSlot.setMemberCode(req.getMemberCode());
        priceSlot.setOrderChannel(req.getOrderChannel());
        priceSlot.setProductPackList(req.getProductPackList());
        priceSlot.setCouponId(req.getCouponId());
    }

    @Override
    public boolean isAccess() {
        PriceCalcReqVO req = this.getSlot().getRequestData();
        if(req != null){
            return true;
        }else{
            return false;
        }
    }
}
