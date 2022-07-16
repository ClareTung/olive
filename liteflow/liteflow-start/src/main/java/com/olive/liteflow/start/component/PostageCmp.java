package com.olive.liteflow.start.component;

import com.olive.liteflow.start.bean.PriceStepVO;
import com.olive.liteflow.start.enums.PriceTypeEnum;
import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 国内运费计算组件
 */
@Component("postageCmp")
public class PostageCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceSlot slot = this.getSlot();

        /**这里Mock运费的策略是：满99免运费，不满99需要10块钱运费**/
        BigDecimal triggerPrice = new BigDecimal(99);
        BigDecimal postage = new BigDecimal(10);
        //先把运费加上去
        BigDecimal prePrice = slot.getLastestPriceStep().getCurrPrice();
        BigDecimal currPrice = prePrice.add(postage);

        slot.addPriceStep(new PriceStepVO(PriceTypeEnum.POSTAGE,
                null,
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                PriceTypeEnum.POSTAGE.getName()));

        //判断运费是否满99了，满了99就去掉运费
        if(prePrice.compareTo(triggerPrice) >= 0){
            prePrice = slot.getLastestPriceStep().getCurrPrice();
            currPrice = currPrice.subtract(postage);

            slot.addPriceStep(new PriceStepVO(PriceTypeEnum.POSTAGE_FREE,
                    null,
                    prePrice,
                    currPrice.subtract(prePrice),
                    currPrice,
                    PriceTypeEnum.POSTAGE_FREE.getName()));
        }
    }
}
