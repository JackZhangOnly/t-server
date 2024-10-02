package com.tstartup.tserver.util;

import com.google.common.collect.Lists;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


public class DateUtil {

    public enum DTFormat {
        yyyy_MM_DD_withdot("yyyy.MMM.dd"),
        yyyy_MM_dd("yyyy-MM-dd"), //
        yyyy_MM_dd_HH("yyyy-MM-dd HH"), //
        yyyy_MM_dd_HH_mm("yyyy-MM-dd HH:mm"), //
        yyyy_MM_dd_HH_mm_ss("yyyy-MM-dd HH:mm:ss"), //
        MM_dd_HH_mm("MM-dd HH:mm"), //
        HH_mm("HH:mm"), //
        HH_mm_ss("HH:mm:ss"), //
        yyyyMMdd("yyyyMMdd"), //
        HHmmss("HHmmss"), //
        yyyyMMddHHmmss("yyyyMMddHHmmss"), //
        yyyyMMddHHmmSSS("yyyyMMddHHmmSSS"), //
        constomFormat("yyyy年MM月dd日HH点"), //
        yyyy("yyyy"), //
        yyyy_MM("yyyy-MM"), //
        EEE_D_MMM_yyyy_HHmmss_GMT("EEE, d MMM yyyy HH:mm:ss 'GMT'"), //
        yyyy_slash_MM_slash_dd("yyyy/MM/dd"), //

        ;

        private String format;
        private int length;

        DTFormat(String format) {
            this.format = format;
            this.length = format.length();
        }

        public String getFormat() {
            return format;
        }

        public int getLength() {
            return length;
        }
    }

    /**
     * 把日期字符串格式化成日期类型
     * @param dateStr
     * @param format
     * @return
     */
    public static Date convert2Date(String dateStr, String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        try {
            simple.setLenient(false);
            return simple.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }

    public static Date convert2DateUTC(String dateStr, String format) {
        SimpleDateFormat simple = new SimpleDateFormat(format);
        try {
            simple.setTimeZone(TimeZone.getTimeZone("UTC"));

            simple.setLenient(false);
            return simple.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return  null;
        }
    }


    /**
     * 把日期类型格式化成字符串
     * @param date
     * @param format
     * @return
     */
    public static String convert2String(Date date, String format) {
        SimpleDateFormat formater = new SimpleDateFormat(format);
        try {
            return formater.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 转sql的time格式
     * @param date
     * @return
     */
    public static java.sql.Timestamp convertSqlTime(Date date){
        java.sql.Timestamp timestamp = new java.sql.Timestamp(date.getTime());
        return timestamp;
    }

    /**
     * 转sql的日期格式
     * @param date
     * @return
     */
    public static java.sql.Date convertSqlDate(Date date){
        java.sql.Date datetamp = new java.sql.Date(date.getTime());
        return datetamp;
    }


    /**
     * 获取当前日期
     * @param format
     * @return
     */
    public static String getCurrentDate(String format) {
        return new SimpleDateFormat(format).format(new Date());
    }

    /**
     * 获取当前日期
     * @date 2018/9/13 下午3:36
     * @return java.util.Date
     */
    public static Date getCurrentDate() {
        return new Date();
    }

    /**
     * 获取时间戳
     * @return
     */
    public static long getTimestamp()
    {
        return System.currentTimeMillis();
    }

    /**
     * 获取月份的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(int year, int month) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month - 1, 1);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取日期的年
     * @param date
     * @return
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取日期的月
     * @param date
     * @return
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获取日期的日
     * @param date
     * @return
     */
    public static int getDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取日期的时
     * @param date
     * @return
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * 获取日期的分种
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    /**
     * 获取日期的秒
     * @param date
     * @return
     */
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    /**
     * 获取星期几
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek-1;
    }

    /**
     * 获取哪一年共有多少周
     * @param year
     * @return
     */
    public static int getMaxWeekNumOfYear(int year) {
        Calendar c = new GregorianCalendar();
        c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
        return getWeekNumOfYear(c.getTime());
    }

    /**
     * 取得某天是一年中的多少周
     * @param date
     * @return
     */
    public static int getWeekNumOfYear(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setMinimalDaysInFirstWeek(7);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 取得某天所在周的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    public static Date getFirstDayBeginOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());

        // 设置时间为00:00:00
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }

    // 获取给定日期所在周的最后一天的24:00:00（即下一天的00:00:00）
    public static Date getLastDayEndOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);

        // 设置时间为24:00:00，即下一天的00:00:00
        c.add(Calendar.DAY_OF_MONTH, 1);  // 增加一天
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        return c.getTime();
    }


    /**
     * 取得某天所在周的第一天
     * @param date
     * @return
     */
    public static Date getFirstDayOfLastWeek(Date date) {
        date = DateUtil.addDays(date, -7);

        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 取得某天所在周的最后一天
     * @param date
     * @return
     */
    public static Date getLastDayOfWeek(Date date) {
        Calendar c = new GregorianCalendar();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    /**
     * 取得某年某周的第一天 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周,2009-01-05为2009年第一周的第一天
     * @param year
     * @param week
     * @return
     */
    public static Date getFirstDayOfWeek(int year, int week) {
        Calendar calFirst = Calendar.getInstance();
        calFirst.set(year, 0, 7);
        Date firstDate = getFirstDayOfWeek(calFirst.getTime());

        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(firstDate);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (week - 1) * 7);
        firstDate = getFirstDayOfWeek(cal.getTime());

        return firstDate;
    }

    /**
     * 取得某年某周的最后一天 对于交叉:2008-12-29到2009-01-04属于2008年的最后一周, 2009-01-04为
     * 2008年最后一周的最后一天
     * @param year
     * @param week
     * @return
     */
    public static Date getLastDayOfWeek(int year, int week) {
        Calendar calLast = Calendar.getInstance();
        calLast.set(year, 0, 7);
        Date firstDate = getLastDayOfWeek(calLast.getTime());

        Calendar firstDateCal = Calendar.getInstance();
        firstDateCal.setTime(firstDate);

        Calendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, Calendar.JANUARY);
        c.set(Calendar.DATE, firstDateCal.get(Calendar.DATE));

        Calendar cal = (GregorianCalendar) c.clone();
        cal.add(Calendar.DATE, (week - 1) * 7);
        Date lastDate = getLastDayOfWeek(cal.getTime());

        return lastDate;
    }

    public static String getFisrtDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    public static String getFirstDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    public static String getLastDayOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public static Date getFirstDayDateOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        return cal.getTime();
    }

    public static Date getFirstDayDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, DateUtil.getYear(date));
        //设置月份
        cal.set(Calendar.MONTH, DateUtil.getMonth(date) - 1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);

        return cal.getTime();
    }

    public static Date getLastDayDateOfMonth(int year,int month) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return cal.getTime();
    }

    public static Date getLastDayDateOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR, DateUtil.getYear(date));
        //设置月份
        cal.set(Calendar.MONTH, DateUtil.getMonth(date) - 1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);

        return cal.getTime();
    }

    private static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        } else {
            Calendar c = Calendar.getInstance();
            c.setTime(date);
            c.add(calendarField, amount);
            return c.getTime();
        }
    }

    /*
     * 1则代表的是对年份操作， 2是对月份操作， 3是对星期操作， 5是对日期操作， 11是对小时操作， 12是对分钟操作， 13是对秒操作，
     * 14是对毫秒操作
     */

    /**
     * 增加年
     * @param date
     * @param amount
     * @return
     */
    public static Date addYears(Date date, int amount) {
        return add(date, 1, amount);
    }

    /**
     * 增加月
     * @param date
     * @param amount
     * @return
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, 2, amount);
    }

    /**
     * 增加周
     * @param date
     * @param amount
     * @return
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, 3, amount);
    }

    /**
     * 增加天
     * @param date
     * @param amount
     * @return
     */
    public static Date addDays(Date date, int amount) {
        return add(date, 5, amount);
    }

    /**
     * 增加时
     * @param date
     * @param amount
     * @return
     */
    public static Date addHours(Date date, int amount) {
        return add(date, 11, amount);
    }

    /**
     * 增加分
     * @param date
     * @param amount
     * @return
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, 12, amount);
    }

    /**
     * 增加秒
     * @param date
     * @param amount
     * @return
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, 13, amount);
    }

    /**
     * 增加毫秒
     * @param date
     * @param amount
     * @return
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, 14, amount);
    }



    /**
     * time差
     * @param before
     * @param after
     * @return
     */
    public static long diffTimes(Date before, Date after){
        return after.getTime() - before.getTime();
    }

    /**
     * 秒差
     * @param before
     * @param after
     * @return
     */
    public static long diffSecond(Date before, Date after){
        return (after.getTime() - before.getTime())/1000;
    }

    /**
     * 分种差
     * @param before
     * @param after
     * @return
     */
    public static int diffMinute(Date before, Date after){
        return (int)((after.getTime() - before.getTime())/1000/60);
    }

    /**
     * 时差
     * @param before
     * @param after
     * @return
     */
    public static int diffHour(Date before, Date after){
        return (int)(after.getTime() - before.getTime())/1000/60/60;
    }

    /**
     * 天数差
     * @param before
     * @param after
     * @return
     */
    public static int diffDay(Date before, Date after) {
        return Integer.parseInt(String.valueOf(((after.getTime() - before.getTime()) / 86400000)));
    }

    /**
     * 月差
     * @param before
     * @param after
     * @return
     */
    public static int diffMonth(Date before, Date after){
        int monthAll=0;
        int yearsX = diffYear(before,after);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(before);
        c2.setTime(after);
        int monthsX = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
        monthAll=yearsX*12+monthsX;
        int daysX =c2.get(Calendar.DATE) - c1.get(Calendar.DATE);
        if(daysX>0){
            monthAll=monthAll+1;
        }
        return monthAll;
    }

    /**
     * 年差
     * @param before
     * @param after
     * @return
     */
    public static int diffYear(Date before, Date after) {
        return getYear(after) - getYear(before);
    }

    /**
     * 设置00:00:00
     * @param date
     * @return
     */
    public static Date setStartDay(Date date) {
        String d = convert2String(date, "yyyy-MM-dd") + " 00:00:00";
        return DateUtil.convert2Date(d, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date setStartDay(String dateStr) {
        Date date = DateUtil.convert2Date(dateStr, DTFormat.yyyy_MM_dd.getFormat());
        return setStartDay(date);
    }

    /**
     * 设置23:59:59
     * @param date
     * @return
     */
    public static Date setEndDay(Date date) {
        String d = convert2String(date, "yyyy-MM-dd") + " 23:59:59";
        return DateUtil.convert2Date(d, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date setEndDay(String dateStr) {
        Date date = DateUtil.convert2Date(dateStr, DTFormat.yyyy_MM_dd.getFormat());
        return setEndDay(date);
    }

    /**
     * 根据类型，返回参数日期的格式化字符串
     *
     * @param date
     * @param formatStr
     */
    public static String getFormatDate(Date date, String formatStr) {
        SimpleDateFormat dateformat = getSimpleDateFormat(formatStr);
        return dateformat.format(date);
    }

    /**
     * SimpleDateFormat线程不安全,每次new开销太大
     * @param format
     * @return
     */
    public static SimpleDateFormat getSimpleDateFormat(String format) {
        ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal<SimpleDateFormat>() {
            @Override
            protected SimpleDateFormat initialValue() {
                return new SimpleDateFormat(format);
            }
        };
        return threadLocal.get();
        //return new SimpleDateFormat(format);
    }

    /**
     * 获取指定日期
     * @return
     */
    public static String getDaysAgo(Integer day) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        calendar.add(Calendar.DATE, day);
        String daysAgo = sdf.format(calendar.getTime());
        return daysAgo;
    }

    /**
     * 获取时间戳-秒
     * @return
     */
    public static Long getDateSeconds(Date date) {
        if (Objects.isNull(date)) {
            return 0L;
        }
        return date.getTime() / 1000;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static int getNowSeconds() {
        return (int) (System.currentTimeMillis() / 1000L);
    }

    /**
     * 返回KSA 时间
     * @param timeStamp 时间戳（毫秒）
     * @return
     */
    public static Long changeTime(Long timeStamp) {
        int num = Math.abs(new Date().getTimezoneOffset() / 60);
        int dif = num -3;

        Long second = (timeStamp + dif * 60 * 60 * 1000) /1000;

        return second;
    }

    /**
     * 将秒数转化为时分秒格式
     *
     * @param time
     * @return
     */
    public static String getSecondFormat(long time) {
        int temp = (int) time;
        int hh = temp / 3600;
        int mm = (temp % 3600) / 60;
        int ss = (temp % 3600) % 60;
        return (hh < 10 ? ("0" + hh) : hh) + ":" +
                (mm < 10 ? ("0" + mm) : mm) + ":" +
                (ss < 10 ? ("0" + ss) : ss);
    }

    /**
     * 获取当前日期（yyyyMMdd）
     * @param date
     * @return
     */
    public static String getCurrentToDay(Date date) {
       return new SimpleDateFormat("yyyyMMdd").format(date.getTime());
    }


    /**
     * 返回指定起始截止日日期内所有日期
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<String> getDatesBetween(Date startDate, Date endDate) {

        LocalDate start = LocalDate.parse(convert2String(startDate, DTFormat.yyyy_MM_dd.getFormat()));
        LocalDate end = LocalDate.parse(convert2String(endDate, DTFormat.yyyy_MM_dd.getFormat()));

        List<String> dates = new ArrayList<>();

        while (!start.isAfter(end)) {
            dates.add(start.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            start = start.plusDays(1);
        }

        return dates;
    }

    /**
     * 返回指定起始截止日日期内所有日期时间戳
     * @param startDate
     * @param endDate
     * @return
     */
    public static List<Long> getDatesLongBetween(Date startDate, Date endDate) {
        List<String> dateStrList = getDatesBetween(startDate, endDate);
        if (!CollectionUtils.isEmpty(dateStrList)) {
            return dateStrList.stream().map(dateStr -> {
                Date date = convert2Date(dateStr, DTFormat.yyyy_MM_dd.getFormat());
                return getDateSeconds(date);
            }).collect(Collectors.toList());
        }

        return Lists.newArrayList();
    }

    /**
     * 根据指定天和小时获取指定月的时间
     * @param monthDate
     * @param givenDay
     * @param givenHour
     * @return
     */
    public static Date getDateByDayAndHour(Date monthDate, int givenDay, int givenHour) {

        // 创建 Calendar 对象并设置为当前日期
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(monthDate);


        // 设置 Calendar 对象的天和小时
        calendar.set(Calendar.DAY_OF_MONTH, givenDay);
        calendar.set(Calendar.HOUR_OF_DAY, givenHour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取结果的 Date 对象
        Date desiredDate = calendar.getTime();

        return desiredDate;
    }


    public static void main(String[] args) {
        /*Calendar cal = Calendar.getInstance();
        System.out.println(cal.get(Calendar.ZONE_OFFSET));


        int num = Math.abs(new Date().getTimezoneOffset() / 60);
        int dif = num -3;

        String dateFirst =  DateUtil.getFisrtDayOfMonth(DateUtil.getYear(new Date()), DateUtil.getMonth(new Date()));
        Date date = DateUtil.convert2Date(dateFirst, DTFormat.yyyy_MM_dd.getFormat());

        Long second = (date.getTime() + dif * 60 * 60 *1000) /1000;
        System.out.println("second======"+second);

        String dateCurStr = DateUtil.convert2String(new Date(), DTFormat.yyyy_MM_dd.getFormat());
        Date dateCur = DateUtil.convert2Date(dateCurStr, DTFormat.yyyy_MM_dd.getFormat());


        System.out.println("secondCur======" + DateUtil.changeTime(dateCur.getTime()));

        System.out.println("secondCurEnd======" + (DateUtil.changeTime(DateUtil.addDays(dateCur, 1).getTime()) -1));*/

        /*System.out.println(DateUtil.getDateSeconds(setStartDay(DateUtil.getCurrentDate())));
        System.out.println(setEndDay(DateUtil.getCurrentDate()));
        Date date = convert2Date("2023-05-04", DTFormat.yyyy_MM_dd.getFormat());
        System.out.println(DateUtil.getDateSeconds(date));
        System.out.println(DateUtil.getDateSeconds(convert2DateUTC("2023-05-04", DTFormat.yyyy_MM_dd.getFormat())));


        */

        System.out.println(calculateAge("2020-1-10"));
/*
        String firstDay = DateUtil.getFormatDate(getFirstDayOfWeek(DateUtil.getCurrentDate()), DTFormat.yyyy_MM_dd.getFormat());

        System.out.println(getCurrentFridayDate());

        System.out.println(getLastFridayDate());*/

        //System.out.println(DateUtil.getFormatDate(getNextWeekFriday(), DTFormat.yyyy_MM_dd.getFormat()));
        Date dateWeek = getFirstDayOfLastWeek(new Date());

        System.out.println(getFormatDate(dateWeek, DTFormat.yyyy_MM_dd.getFormat()));

        Date date = DateUtil.getFirstDayBeginOfWeek(DateUtil.getCurrentDate());
        System.out.println(DateUtil.getDateSeconds(date));

        System.out.println(DateUtil.getDateSeconds(DateUtil.getLastDayEndOfWeek(DateUtil.getCurrentDate())));

    }


    /**
     * 获取上个月开始时间戳
     */
    public static long getLastMonthStart() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();

        // 将日期设置为上个月
        calendar.add(Calendar.MONTH, -1);

        // 设置为上个月的第一天的开始时间
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // 获取上个月的第一天的时间戳
        return calendar.getTimeInMillis() / 1000;
    }

    public static long getLastMonthEnd() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();
        // 设置为上个月的最后一天的结束时间
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, lastDay);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        // 获取上个月的最后一天的时间戳
        return calendar.getTimeInMillis() / 1000;
    }

    /**
     * 计算
     * @param dob
     * @return
     */
    public static int calculateAge(Date dob) {
        Calendar birthCalendar = Calendar.getInstance();
        birthCalendar.setTime(dob);

        Calendar currentCalendar = Calendar.getInstance();

        int age = currentCalendar.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);

        // Check if the birthday has occurred this year
        if (currentCalendar.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public static int calculateAge(String dobString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dob = dateFormat.parse(dobString);
            int age = calculateAge(dob);

            return age;
        } catch (ParseException e) {
            System.out.println("日期格式错误，请使用yyyy-MM-dd格式。");
        }

        return 0;
    }

    public static Date getCurrentFridayDate() {
        // 获取当前日期的Calendar实例
        Calendar calendar = Calendar.getInstance();

        // 设置到本周的周五
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);

        // 获取年、月、日
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始计算
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dateStr = year + "-" + month + "-" + day;

        return convert2Date(dateStr, DTFormat.yyyy_MM_dd.getFormat());
    }

    public static Date getLastFridayDate() {
        // 获取当前日期的Calendar实例
        Calendar calendar = Calendar.getInstance();

        // 设置到本周的第一天（默认是周日）
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // 减去7天以回到上周
        calendar.add(Calendar.DATE, -7);

        // 再添加4天，以到达上周五
        calendar.add(Calendar.DATE, 4);

        // 获取年、月、日
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 月份从0开始计算
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String dateStr = year + "-" + month + "-" + day;

        return convert2Date(dateStr, DTFormat.yyyy_MM_dd.getFormat());
    }

    public static Date getNextWeekFriday() {
        // 获取当前日期
        Calendar calendar = Calendar.getInstance();

        // 计算下周五的日期
        int daysUntilNextFriday = Calendar.FRIDAY - calendar.get(Calendar.DAY_OF_WEEK);
        if (daysUntilNextFriday <= 0) {
            // 如果今天是星期五或者之后的星期，则需要加上7天以确保是下周的星期五
            daysUntilNextFriday += 7;
        }
        // 总是增加7天，以确保是下一周
        calendar.add(Calendar.DAY_OF_YEAR, daysUntilNextFriday);

        // 输出下周五的日期
        return  calendar.getTime();
    }

    /**
     * 是否在星期五之后
     * @return
     */
    public static boolean boolPassFriday() {
        Date nowDate = getCurrentDate();

        Date fridayDate = getCurrentFridayDate();

        return nowDate.getTime() > fridayDate.getTime();
    }


}
