package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.report.SalesVolume;
import com.zy.vo.SalesVolumeListVo;
import org.springframework.stereotype.Component;

/**
 * Author: Xuwq
 * Date: 2017/8/24.
 */
@Component
public class SalesVolumeComponent {
    public SalesVolumeListVo buildSalesVolumeListVo(SalesVolume salesVolume) {
        SalesVolumeListVo salesVolumeListVo = new SalesVolumeListVo();
        BeanUtils.copyProperties(salesVolume, salesVolumeListVo);
        return salesVolumeListVo;
    }
}
