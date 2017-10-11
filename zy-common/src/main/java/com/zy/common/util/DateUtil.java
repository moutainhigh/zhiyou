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

    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
     * 查询 指定时间 时间所属月份
     * @param date
     * @return
     */
    public static int getDay(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal.get(Calendar.DATE);
    }

    /**
     * 查询 当前 时间所属年份
     * @param date
     * @return
     */
    public static int getYear(Date date){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
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
     * 格式化数为2位字符串
     * @param d
     * @return
     */
    public static String formatString(Object d) {
        DecimalFormat df = new DecimalFormat("###0.00");
        df.setGroupingUsed(false);
        return df.format(d);
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
     * 将数组转成字符串
     * @return
     */
    public static String stringarryToString(String[] arryString,boolean flag){
        StringBuffer str = new StringBuffer("");
        if (arryString==null&&arryString.length==0){
            return str.toString();
        }
        if (!flag) {
            for (int i = 0; i < arryString.length; i++) {
                str.append(arryString[i]).append(",");
            }
        }else{
            for (int i = arryString.length-1; i >=0; i--) {
                str.append(arryString[i]).append(",");
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


    public static int getAge(Date birthDate) {

        if (birthDate == null){
            throw new RuntimeException("出生日期不能为null");
        }
        int age = 0;

        Date now = new Date();

        SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
        SimpleDateFormat format_M = new SimpleDateFormat("MM");

        String birth_year = format_y.format(birthDate);
        String this_year = format_y.format(now);

        String birth_month = format_M.format(birthDate);
        String this_month = format_M.format(now);

        // 初步，估算
        age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);

        // 如果未到出生月份，则age - 1
        if(this_month.compareTo(birth_month) < 0){
            age -= 1;
        }
        if (age <0){
            age = 0;
        }
        return age;
    }





    /**
     * 获取时间 相差 天数
     * @param beginDate
     * @param endDate
     * @return
     */
    public static int calculateDiffDays(java.util.Date beginDate, java.util.Date endDate) {
        if (beginDate == null || endDate == null) {
            return 0;
        } else {
            long bMillSeconds = beginDate.getTime();
            long eMillSeconds = endDate.getTime();
            long temp = eMillSeconds - bMillSeconds;
            double t1 = temp / 1000 / 60 / 60 / 24;
            return (new Double(t1)).intValue();
        }
    }

    /**
     * 计算两个日期天数
     * @param time1
     * @param time2
     * @return
     */
    public static long getIntervalDays(String time1, String time2) {
        long quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            quot = date2.getTime() - date1.getTime();
            quot = quot / 1000 / 60 / 60 / 24;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }
    /**
     *根据身份证  获取年龄
     * @param IDCardNum
     * @return
     */
    public  static int getAge(String IDCardNum){
        int year, month, day, idLength = IDCardNum.length();
        Calendar cal1 = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        if(idLength == 18){
            year = Integer.parseInt(IDCardNum.substring(6,10));
            month = Integer.parseInt(IDCardNum.substring(10,12));
            day = Integer.parseInt(IDCardNum.substring(12,14));
        }else if(idLength == 15){
            year = Integer.parseInt(IDCardNum.substring(6,8)) + 1900;
            month = Integer.parseInt(IDCardNum.substring(8,10));
            day = Integer.parseInt(IDCardNum.substring(10,12));
        }else {
            System.out.println("This ID card number is invalid!");
            return -1;
        }
        cal1.set(year, month, day);
        return getYearDiff(today, cal1);
    }

    public static int getYearDiff(Calendar cal, Calendar cal1){
        int m = (cal.get(cal.MONTH)+1) - (cal1.get(cal1.MONTH));
        int y = (cal.get(cal.YEAR)) - (cal1.get(cal1.YEAR));
        int date = (cal.get(cal.DATE))-(cal1.get(cal1.DATE));
        if(date>=0){
            return (y * 12 + m)/12;
        }
        return (y * 12 + m-1)/12;
    }


    /**
     *格式化对象数据为指定长度的字符串
     * @param sum 数据
     * @param i 转化长度
     * @return
     */
    public static String toStringLength(Object sum, int i) {
      String sumStr = sum.toString();
        if (sumStr.length()>i){//大于长度 要截取
            return sumStr.substring(0,i);
        } else if (sumStr.length()<i){
            int leng =i-sumStr.length();
          for (int j=0;j<leng;j++){
              sumStr ="0"+sumStr;
          }
        }
       return sumStr;
    }


    /**
     * 从字符串里面提取 月份 月份可能是两位
     * @param str
     * @return
     */
    public static  int StringtoInt(String str,int length ){
        String  m1= str.substring(length,length+1);
        String  m2= str.substring(length+1, length+2);
        try {
            Integer.parseInt(m2);
            return new Integer(m1+m2);
        } catch (NumberFormatException e) {
            return new Integer(m1);
        }

    }


    public static void  main(String []age) throws ParseException {
        System.out.println(DateFormatUtils.format( DateUtil.getBeforeMonthBegin(new Date(),0,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthEnd(new Date(),1,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        System.out.println(DateUtil.getMoth(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        System.out.println("-------------------");
        System.out.println(new Date().after(DateUtil.getDateEnd(DateUtil.getMonthData(new Date(),0,-1))));
        System.out.println("----------##########--");
        System.out.println(DateFormatUtils.format( DateUtil.getDateEnd(DateUtil.getMonthData(new Date(),0,-1)),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-2,0)),"yyyy-MM-dd HH:mm:ss"));
        System.out.println("-------------------");
        System.out.println(DateUtil.getDay(DateUtil.getMonthData(new Date(),0,-1)));
        System.out.println("---------1----------");
        System.out.println(DateFormatUtils.format(DateUtil.getCurrYearFirst(),"yyyy-MM-dd HH:mm:ss"));
        System.out.println("-------------------");
        System.out.println(DateUtil.getMothNum(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        System.out.println(DateUtil.getYear(DateUtil.getBeforeMonthBegin(new Date(),-1,0)));
        System.out.println("----------##########--");
        System.out.println(DateUtil.toStringLength(123343342232L,6));
        System.out.println(DateUtil.formatString(123));
        System.out.println(StringtoInt("2017年10月推荐奖",5));


        System.out.println("----------???????????--");
        System.out.println(DateUtil.getDaysOfMonth(new Date()));

      /*  ystem.out.println(DateFormatUtils.format( DateUtil.getDateEnd(new Date()),"yyyy-MM-dd HH:mm:ss"));
        System.out.println("年龄:"+getAge("411528201607192688"));
        System.out.println(DateUtil.getMoth(new Date()));
      ;*/
       /*  System.out.println(DateUtil.getMoth(new Date()));
    public static void  main(String []age){
       System.out.println(DateFormatUtils.format( DateUtil.getBeforeMonthBegin(new Date(),-6,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateFormatUtils.format(DateUtil.getBeforeMonthEnd(new Date(),-5,0),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(DateUtil.getMoth(new Date()));
        System.out.println(DateUtil.formatDouble(10123456789.0));
        String ll="qwertyui";
        System.out.println("1111"+ll.contains("ty"));*/
      /*  System.out.println(DateFormatUtils.format(getMonthData(new Date(),3,-1),"yyyy-MM-dd HH:mm:ss"));
        System.out.println(getMothNum(getMonthData(new Date(),3,-1)));

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
