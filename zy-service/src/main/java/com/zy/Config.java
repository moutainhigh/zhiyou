package com.zy;

import com.zy.entity.fnc.CurrencyType;
import com.zy.entity.usr.User.UserType;

import java.math.BigDecimal;

public interface Config {

    Long getSysUserId();

    BigDecimal getWithdrawFeeRate(UserType userType, CurrencyType currencyType);

    boolean isDev();

}
