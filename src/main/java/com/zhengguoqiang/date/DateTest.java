package com.zhengguoqiang.date;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author zhengguoqiang
 */
public class DateTest {
    public static void main(String[] args) throws InterruptedException {
        /*Date date = new Date();
        System.out.println(date);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        for (int i = 0; i < 30; i++) {
            new Thread(() -> {
                for (int j = 0; j < 100; j++) {
                    Date parseDate = null;
                    try {
                        sdf.parse("20190806");
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    System.out.println(parseDate);
                }
            }).start();
        }*/

//        testLocalDate();
//        testLocalTime();
//        testLocalDateTime();
//        testInstant();
//        testDuration();
//        testPeriod();
//        testDateFormat();
        tranferDateToLocalDate();
    }

    private static void testLocalDate() {
        LocalDate localDate = LocalDate.of(2019, 8, 6);
        System.out.println(localDate.getYear());
        System.out.println(localDate.getMonth());
        System.out.println(localDate.getMonthValue());
        System.out.println(localDate.getDayOfYear());
        System.out.println(localDate.getDayOfMonth());
        System.out.println(localDate.getDayOfWeek());
    }

    private static void testLocalTime(){
        LocalTime localTime = LocalTime.now();
        System.out.println(localTime.getHour());
        System.out.println(localTime.getMinute());
        System.out.println(localTime.getSecond());
    }

    private static void testLocalDateTime(){
        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();
        LocalDateTime localDateTime = LocalDateTime.of(localDate,localTime);
        System.out.println(localDateTime.toString());
    }

    private static void testInstant() throws InterruptedException {
        Instant start = Instant.now();
        Thread.sleep(1000);
        Instant end = Instant.now();
        Duration duration = Duration.between(start, end);
        System.out.println(duration.toMillis());
    }

    private static void testDuration(){
        LocalTime start = LocalTime.now();
        LocalTime end = start.minusHours(1);
        Duration between = Duration.between(start, end);
        System.out.println(between.toHours());
    }

    private static void testPeriod(){
        Period period = Period.between(LocalDate.of(2019,5,1),
                LocalDate.of(2019,8,1));
        System.out.println(period.getYears());
        System.out.println(period.getMonths());
        System.out.println(period.getDays());
    }

    private static void testDateFormat(){
        LocalDate localDate = LocalDate.now();
        String format = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        String format1 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        System.out.println(format);
        System.out.println(format1);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(localDate.format(dateTimeFormatter));
    }

    private static void testParse(){
        String date = "20190806";
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.BASIC_ISO_DATE);
        System.out.println(localDate.getYear());
    }

    private static void tranferDateToLocalDate(){

        /*
            1. Date -> java.time
            Date -> Instant + System default time zone = LocalDate
            Date -> Instant + System default time zone = LocalDateTime
            Date -> Instant + System default time zone = ZonedDateTime
            Date == Instant
        */

        //Asia/Shanghai
        ZoneId defaultZoneId = ZoneId.systemDefault();
        System.out.println("System Default TimeZone:" + defaultZoneId);

        Date date = new Date();
        System.out.println("date:" + date);

        //1. Convert Date -> Instant
        Instant instant = date.toInstant();
        System.out.println("instant:" + instant); //Zone:UTC+0

        //2. Instant + system default time zone + toLocalDate() = LocalDate
        LocalDate localDate = instant.atZone(defaultZoneId).toLocalDate();
        System.out.println("localDate:" + localDate);

        //3. Instant + system default time zone + toLocaleDateTime() = LocalDateTime
        LocalDateTime localDateTime = instant.atZone(defaultZoneId).toLocalDateTime();
        System.out.println("localDateTime:" + localDateTime);

        //4. Instant + system default time zone = ZonedDateTime
        ZonedDateTime zonedDateTime = instant.atZone(defaultZoneId);
        System.out.println("zonedDateTime:" + zonedDateTime);

        System.out.println("=======================================================");

        /*
            3. java.time -> Date
         */
        LocalDate localDate1 = LocalDate.now();
        Date from = Date.from(localDate1.atStartOfDay(defaultZoneId).toInstant());
        System.out.println("localDate:" + localDate1);
        System.out.println("date:" + from);

        LocalDateTime localDateTime1 = LocalDateTime.now();
        Date from1 = Date.from(localDateTime1.atZone(defaultZoneId).toInstant());
        System.out.println("localDateTime:" + localDateTime1);
        System.out.println("date2:" + from1);

        ZonedDateTime zonedDateTime1 = localDateTime1.atZone(defaultZoneId);
        Date date3 = Date.from(zonedDateTime1.toInstant());
        System.out.println("ZonedDateTime:" + zonedDateTime1);
        System.out.println("date3:" + date3);
    }
}
