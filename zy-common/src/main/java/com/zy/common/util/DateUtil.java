package com.zy.common.util;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by it001 on 2017/6/9.
 */
public class DateUtil {

    /**
     * 默认日期格式
     */
    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm";


    /**
     * 格式化日期
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date){
        SimpleDateFormat f = new SimpleDateFormat(TIME_PATTERN);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取当年的第一天
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }



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
        calendar.setTime(getMonthData(date,moth,year));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个
      /*  calendar.add(Calendar.MONTH, moth);*/
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取当前日期下的结束时间
     * @param date
     */
    public static Date getDateEnd(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 获取指定月份的第一天的开始
     * @param date
     */
    public static Date getMonthBegin(Date date,int month,int year){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.MONTH, month-1);
        calendar.add(Calendar.YEAR, year);
        return calendar.getTime();
    }

    /**
     * 获取指定月份相差月份日期
     * @param date
     * @param month
     * @return
     */
    public static Date getMonthData(Date date,int month,int dateI){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, month);
        calendar.add(Calendar.DATE,dateI);
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
     * 查询 指定时间 时间所属月份
     * @param date
     * @return
     */
    public static int getMothNum(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.MONTH)+1;
    }

    /**
     * 获取指定时间的周信息下标
     * @param date
     * @return
     */
    public static int getWeekNum(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DAY_OF_WEEK);
    }
    /**
     * 获取指定时间的周信息的名字
     * @param date
     * @return
     */
    public static String getWeek(Date date){
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        int week = DateUtil.getWeekNum(date)-1;
        if (week < 0) {
            week = 0;
        }
        return weekDays[week];
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
        return DateUtil.formatDouble(((double)coun/(double)data)*100);
    }

    /**
     * 获取两个时差之间的月份
     * @param minDate
     * @param maxDate
     * @return
     */
    public static List<String> getMonthBetween(Date minDate, Date maxDate) {
        List<String> result = new ArrayList<String>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(minDate);
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(maxDate);
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }

        return result;
    }
    public static void  main(String []age) throws ParseException {
       System.out.println(DateFormatUtils.format( DateUtil.getBeforeMonthBegin(new Date(),-2,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthEnd(new Date(),-1,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format( DateUtil.getDateEnd(new Date()),"yyyy-MM-dd HH:mm:ss"));
       /*  System.out.println(DateUtil.getMoth(new Date()));
        System.out.println(DateUtil.formatDouble(10123456789.0));
        String ll="qwertyui";
        System.out.println("1111"+ll.contains("ty"));*/
      /*  System.out.println(DateFormatUtils.format(getMonthData(new Date(),3,-1),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(getMothNum(getMonthData(new Date(),3,-1)));
        System.out.println(getMothNum(new Date()));
        Date date = new SimpleDateFormat("yyyy-MM").parse("2005-06");
        System.out.println(DateFormatUtils.format(date,"yyyy-MM-dd HH:mm:ss"));
        List<String> list= getMonthBetween(new Date(),getMonthData(new Date(),3,-1));
        for(String s:list){
            System.out.println(s);
        }*/
      /*  System.out.println(getWeekNum(new Date()));
        System.out.println(getWeek(new Date()));*/


       /* dataMap.put("operatedTimeBegin", DateUtil.getBeforeMonthBegin(new Date(),0,0));
        dataMap.put("operatedTimeEnd",DateUtil.getBeforeMonthEnd(new Date(),1,0));*/

      //  System.out.print(((double)1l/(double)2l));
    }


}
