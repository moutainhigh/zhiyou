package com.zy;

import com.gc.entity.fnc.CurrencyType;
import com.gc.entity.usr.User.UserType;

import java.math.BigDecimal;

public interface Config {

    Long getSysUserId();

    Long getFeeUserId();

    Long getGrantUserId();

    BigDecimal getWithdrawFeeRate(UserType userType, CurrencyType currencyType);

    boolean isDev();

}
