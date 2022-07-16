package com.olive.liteflow.start.component;

import com.olive.liteflow.start.bean.PriceStepVO;
import com.olive.liteflow.start.enums.PriceTypeEnum;
import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 优惠券抵扣计算组件
 */
@Component("couponCmp")
public class CouponCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceSlot slot = this.getSlot();

        /**这里Mock下根据couponId取到的优惠卷面值为15元**/
        Long couponId = slot.getCouponId();
        BigDecimal couponPrice = new BigDecimal(15);

        BigDecimal prePrice = slot.getLastestPriceStep().getCurrPrice();
        BigDecimal currPrice = prePrice.subtract(couponPrice);

        slot.addPriceStep(new PriceStepVO(PriceTypeEnum.COUPON_DISCOUNT,
                couponId.toString(),
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                PriceTypeEnum.COUPON_DISCOUNT.getName()));
    }

    @Override
    public boolean isAccess() {
        PriceSlot slot = this.getSlot();
        if(slot.getCouponId() != null){
            return true;
        }else{
            return false;
        }
    }
}
