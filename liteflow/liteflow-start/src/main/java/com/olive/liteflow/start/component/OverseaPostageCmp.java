package com.olive.liteflow.start.component;

import com.olive.liteflow.start.bean.PriceStepVO;
import com.olive.liteflow.start.enums.PriceTypeEnum;
import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeComponent;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 境外购运费计算组件
 */
@Component("overseaPostageCmp")
public class OverseaPostageCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceSlot slot = this.getSlot();

        /**这里Mock境外购运费的策略是：不管多少钱，都要加上15元运费**/
        BigDecimal postage = new BigDecimal(15);
        BigDecimal prePrice = slot.getLastestPriceStep().getCurrPrice();
        BigDecimal currPrice = prePrice.add(postage);

        slot.addPriceStep(new PriceStepVO(PriceTypeEnum.OVERSEAS_POSTAGE,
                null,
                prePrice,
                currPrice.subtract(prePrice),
                currPrice,
                PriceTypeEnum.OVERSEAS_POSTAGE.getName()));
    }
}
