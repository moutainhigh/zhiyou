package com.zy.component;

import com.zy.common.util.BeanUtils;
import com.zy.entity.report.SalesVolume;
import com.zy.entity.sys.SystemCode;
import com.zy.service.SystemCodeService;
import com.zy.vo.SalesVolumeExportVo;
import com.zy.vo.SalesVolumeListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Author: Xuwq
 * Date: 2017/8/24.
 */
@Component
public class SalesVolumeComponent {

    @Autowired
    private SystemCodeService systemCodeService;


    public SalesVolumeListVo buildSalesVolumeListVo(SalesVolume salesVolume) {
        SalesVolumeListVo salesVolumeListVo = new SalesVolumeListVo();
        BeanUtils.copyProperties(salesVolume, salesVolumeListVo);
        String largeArea = salesVolume.getAreaType();
        if(largeArea != null){
            SystemCode largeAreaType = systemCodeService.findByTypeAndValue("LargeAreaType", largeArea);
            salesVolumeListVo.setAreaType(largeAreaType.getSystemName());
        }
        return salesVolumeListVo;
    }

    public SalesVolumeExportVo buildSalesVolumeExportVo(SalesVolume salesVolume) {
        SalesVolumeExportVo salesVolumeExportVo = new SalesVolumeExportVo();
        BeanUtils.copyProperties(salesVolume, salesVolumeExportVo);
        if (salesVolume.getType() != null && salesVolume.getNumber() != null){
            if (salesVolume.getType() == 1){
                salesVolumeExportVo.setRankDetail("+   " + salesVolume.getNumber());
            }else if (salesVolume.getType() == 2){
                salesVolumeExportVo.setRankDetail("=");
            }else if (salesVolume.getType() == 3){
                salesVolumeExportVo.setRankDetail("-   " + salesVolume.getNumber());
            }
        }
        if (salesVolume.getIsBoss() == 1){
            salesVolumeExportVo.setIsBoss("是");
        }else if (salesVolume.getIsBoss() == 0){
            salesVolumeExportVo.setIsBoss("否");
        }
        String largeArea = salesVolume.getAreaType();
        if(largeArea != null){
            SystemCode largeAreaType = systemCodeService.findByTypeAndValue("LargeAreaType", largeArea);
            salesVolumeExportVo.setAreaType(largeAreaType.getSystemName());
        }
        return salesVolumeExportVo;
    }
}
