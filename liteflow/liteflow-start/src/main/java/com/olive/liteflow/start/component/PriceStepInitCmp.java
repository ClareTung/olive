package com.olive.liteflow.start.component;

import com.olive.liteflow.start.bean.PriceStepVO;
import com.olive.liteflow.start.bean.ProductPackVO;
import com.olive.liteflow.start.enums.PriceTypeEnum;
import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeComponent;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * 价格步骤初始化器(把原价初始化进去)
 */
@Component("priceStepInitCmp")
public class PriceStepInitCmp extends NodeComponent {
    @Override
    public void process() throws Exception {
        PriceSlot priceSlot = this.getSlot();

        //初始化价格步骤
        List<ProductPackVO> packList = priceSlot.getProductPackList();
        BigDecimal totalOriginalPrice = new BigDecimal(0);
        for(ProductPackVO packItem : packList){
            totalOriginalPrice = totalOriginalPrice.add(packItem.getSalePrice().multiply(new BigDecimal(packItem.getCount())));
        }
        priceSlot.addPriceStep(new PriceStepVO(PriceTypeEnum.ORIGINAL,
                null,
                null,
                totalOriginalPrice,
                totalOriginalPrice,
                PriceTypeEnum.ORIGINAL.getName()));
        priceSlot.setOriginalOrderPrice(totalOriginalPrice);
    }

    @Override
    public boolean isAccess() {
        PriceSlot slot = this.getSlot();
        if(CollectionUtils.isNotEmpty(slot.getProductPackList())){
            return true;
        }else{
            return false;
        }
    }
}
