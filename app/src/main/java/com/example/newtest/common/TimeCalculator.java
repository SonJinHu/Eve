package com.example.newtest.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TimeCalculator {

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.KOREA);

    public static String getCurrentTime() {
        return dateFormat.format(new Date());
    }

    public static String calculate(String dateString) throws ParseException {
        Calendar cal = Calendar.getInstance();

        // 현재 시간
        cal.setTimeInMillis(System.currentTimeMillis());
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1; // from 0 (January) to 11 (December)
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

        // 댓글 작성 일자
        cal.setTime(dateFormat.parse(dateString));
        int comment_year = cal.get(Calendar.YEAR);
        int comment_month = cal.get(Calendar.MONTH) + 1; // from 0 (January) to 11 (December)
        int comment_day = cal.get(Calendar.DAY_OF_MONTH);
        int comment_hour = cal.get(Calendar.HOUR_OF_DAY);
        int comment_min = cal.get(Calendar.MINUTE);
        int comment_sec = cal.get(Calendar.SECOND);

        if (year - comment_year != 0) {
            return year - comment_year + "년 전";
        } else if (month - comment_month != 0) {
            return month - comment_month + "달 전";
        } else if (day - comment_day != 0) {
            if (day - comment_day < 7) {
                return day - comment_day + "일 전";
            }
            return (day - comment_day) / 7 + "주 전";
        } else if (hour - comment_hour != 0) {
            return hour - comment_hour + "시간 전";
        } else if (min - comment_min != 0) {
            return min - comment_min + "분 전";
        } else {
            //return sec - comment_sec + "초 전";
            return "방금 전";
        }
    }

}
