package com.zy.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

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
        calendar.set(Calendar.HOUR_OF_DAY, 0);
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
        calendar.set(Calendar.HOUR_OF_DAY, 23);
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

    /**
     * 将数组转成字符串
     * @return
     */
    public static String arryToString(double[]arry,boolean flag){
        StringBuffer str = new StringBuffer("");
        DecimalFormat df = new DecimalFormat("###0.00");
        if (arry==null&&arry.length==0){
            return str.toString();
        }
        if (!flag) {
            for (int i = 0; i < arry.length; i++) {
                str.append(df.format(arry[i])).append(",");
            }
        }else{
            for (int i = arry.length-1; i >=0; i--) {
                str.append(df.format(arry[i])).append(",");
            }
        }
        if (str.length() > 0) {
            str.delete(str.length() - 1, str.length());
        }
        return str.toString();
    }
    /**
     * 将数组转成字符串
     * @return
     */
    public static String longarryToString(long[]arryLong,boolean flag){
        StringBuffer str = new StringBuffer("");
        if (arryLong==null&&arryLong.length==0){
            return str.toString();
        }
        if (!flag) {
            for (int i = 0; i < arryLong.length; i++) {
                str.append(arryLong[i]).append(",");
            }
        }else{
            for (int i = arryLong.length-1; i >=0; i--) {
                str.append(arryLong[i]).append(",");
            }
        }
        if (str.length() > 0) {
            str.delete(str.length() - 1, str.length());
        }
        return str.toString();
    }

    /**
     * 计算百分比 并格式化成2位小数
     * @param arrydata
     * @param data
     * @return
     */
    public  static Double countPro(long[]arrydata,long data){
        long coun =0;
       if (data==0){
           return 0d;
       }
        for (int i=0;i<arrydata.length;i++){
            coun=coun+arrydata[i];
        }
     return DateUtil.formatDouble(coun/data*100);
    }
    public static void  main(String []age){
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthBegin(new Date(),-1,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthEnd(new Date(),0,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.getMoth(new Date()));
        System.out.println(DateUtil.formatDouble(10123456789.0));
    }


}
