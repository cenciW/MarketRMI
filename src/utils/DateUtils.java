package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

    public static String dateToString(Date date) {

        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        return formatter.format(zonedDateTime);
    }

    public static String dateToLocalString(Date date) {

        Instant instant = date.toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());

        return formatter.format(zonedDateTime);
    }

    public static Date getUtcNow() {
        Instant instant = new Date().toInstant();
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

        return Date.from(zonedDateTime.toInstant());
    }

    public static Date stringToDate(String dateString) throws Exception {
        try {

            Instant instant = Instant.parse(dateString + "Z");

            ZonedDateTime zonedDateTime = instant.atZone(ZoneId.of("UTC"));

            return Date.from(zonedDateTime.toInstant());

        } catch (Exception e) {
            throw new Exception("Erro ao converter string para data");
        }
    }



}
