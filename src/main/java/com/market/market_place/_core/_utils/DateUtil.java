package com.market.market_place._core._utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    public static String timestampFormat(Timestamp startDate) {

        Date currentDate = new Date(startDate.getTime());

        return DateFormatUtils.format(currentDate, "yyyy년 MM월 dd일 HH시 mm분");
    }

    public static String chatFormat(Timestamp startDate) {

        Date currentDate = new Date(startDate.getTime());

        return DateFormatUtils.format(currentDate, "오후 HH:mm");
    }


    public static String dateTimeFormat(Timestamp endDate) {
        if (endDate == null) {
            return "";
        }

        LocalDateTime localDateTime = endDate.toLocalDateTime();
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));
    }
}

