package com.zy.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by it001 on 2017/6/9.
 */
public class DateUtil {

    /**
     * 获取之前月份的第一天的开始
     * @param date
     */
    public static Date getBeforeMonthBegin(Date date,int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取之前月份的最后一天的结束时间
     * @param date
     */
    public static Date getBeforeMonthEnd(Date date,int moth,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个
        calendar.add(Calendar.MONTH, moth);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 查询 当前 时间所属月份
     * @param date
     * @return
     */
    public static int getMoth(Date date){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH) + 1;
    }

    /**
     * 格式化数为2为
     * @param d
     * @return
     */
    public static Double formatDouble(double d) {
        DecimalFormat df = new DecimalFormat("###0.00");
        df.setGroupingUsed(false);
        return Double.valueOf(df.format(d));
    }
    public static void  main(String []age){
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthBegin(new Date(),-1,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthEnd(new Date(),-0,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.print(DateUtil.getMoth(new Date()));
    }
}
