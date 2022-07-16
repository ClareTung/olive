package com.olive.liteflow.start.component;

import com.olive.liteflow.start.slot.PriceSlot;
import com.yomahub.liteflow.core.NodeCondComponent;
import org.springframework.stereotype.Component;

/**
 * 运费条件组件
 */
@Component("postageCondCmp")
public class PostageCondCmp extends NodeCondComponent {
    @Override
    public String processCond() throws Exception {
        PriceSlot priceSlot = this.getSlot();
        //根据参数oversea来判断是否境外购，转到相应的组件
        boolean oversea = priceSlot.isOversea();
        if(oversea){
            return "overseaPostageCmp";
        }else{
            return "postageCmp";
        }
    }
}
