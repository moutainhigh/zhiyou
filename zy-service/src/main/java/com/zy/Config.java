package com.zy;

import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.usr.User.UserType;

import java.math.BigDecimal;
import java.util.Date;

public interface Config {

    Long getSysUserId();

    BigDecimal getWithdrawFeeRate(UserType userType, CurrencyType currencyType);

    boolean isDev();

    boolean isOld(Long productId);

    Long getOld();

    Long getNew();

    boolean isWithdrawOn();

    boolean isOpenOrderFill();

    Date getOrderFillTime();
}
