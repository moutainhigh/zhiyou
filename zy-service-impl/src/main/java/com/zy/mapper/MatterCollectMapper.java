package com.zy.mapper;

import com.zy.entity.cms.MatterCollect;
import com.zy.model.query.MatterQueryModel;

/**
 * Author: Xuwq
 * Date: 2017/4/5.
 */
public interface MatterCollectMapper {

    MatterCollect queryMatterCollect(MatterQueryModel matterQueryModel);

    void delete(MatterQueryModel matterQueryModel);

    void insert(MatterQueryModel matterQueryModel);
}
