package com.gc.entity.usr;

import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.MONTH;

import java.util.Calendar;
import java.util.Date;

public enum Horoscope {
    白羊座, 金牛座, 双子座, 巨蟹座, 狮子座, 处女座, 天秤座, 天蝎座, 射手座, 魔羯座, 水瓶座, 双鱼座,;
    public static final Horoscope[] horoscopes = {水瓶座, 双鱼座, 白羊座, 金牛座, 双子座, 巨蟹座, 狮子座, 处女座, 天秤座, 天蝎座, 射手座, 魔羯座};
    public static final int[] constellationEdgeDay = {20, 19, 21, 21, 21, 22, 23, 23, 23, 23, 22, 22};

    public final static Horoscope horoscope(Date date) {
        if (date == null) {
            throw new NullPointerException("date cannot be null");
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(MONTH);
        int day = cal.get(DAY_OF_MONTH);
        if (day < constellationEdgeDay[month]) {
            month = month - 1;
        }
        if (month >= 0) {
            return horoscopes[month];
        }
        return horoscopes[11];
    }
    public static Horoscope valueOf(int ordinal) {
        if (ordinal < 0 || ordinal >= values().length) {
            throw new IndexOutOfBoundsException("Invalid ordinal");
        }
        return values()[ordinal];
    }

}