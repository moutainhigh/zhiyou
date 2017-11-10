package com.zy.service;

import java.math.BigDecimal;

/**
 * Created by it001 on 2017-11-05.
 */
public interface UserCheckService {

    void checkUserLevel(Long id, Long quantity,Integer prodectType);

    void editOderStoreIn(Long orderId,Long userId,Integer productType);

    void editOrderStoreOut(Long orderId,Long userId,Integer productType);

}
