package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.report.SalesVolume;
import com.zy.vo.SalesVolumeExportVo;
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

    public SalesVolumeExportVo buildSalesVolumeExportVo(SalesVolume salesVolume) {
        SalesVolumeExportVo salesVolumeExportVo = new SalesVolumeExportVo();
        BeanUtils.copyProperties(salesVolume, salesVolumeExportVo);
        if (salesVolume.getType() == 1){
            salesVolumeExportVo.setRankDetail("+   " + salesVolume.getNumber());
        }else if (salesVolume.getType() == 2){
            salesVolumeExportVo.setRankDetail("=");
        }else if (salesVolume.getType() == 3){
            salesVolumeExportVo.setRankDetail("-   " + salesVolume.getNumber());
        }
        if (salesVolume.getAreaType() == 1){
            salesVolumeExportVo.setAreaType("东");
        }else if (salesVolume.getAreaType() == 2){
            salesVolumeExportVo.setAreaType("南");
        }else if (salesVolume.getAreaType() == 3){
            salesVolumeExportVo.setAreaType("西");
        }else if (salesVolume.getAreaType() == 4){
            salesVolumeExportVo.setAreaType("北");
        }else if (salesVolume.getAreaType() == 5){
            salesVolumeExportVo.setAreaType("中");
        }
        return salesVolumeExportVo;
    }
}
