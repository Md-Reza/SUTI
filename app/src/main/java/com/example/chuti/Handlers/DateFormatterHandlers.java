package com.example.chuti.Handlers;

import android.annotation.SuppressLint;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatterHandlers {
    static SimpleDateFormat parseSimpleDateFormat, formatter_WorkDateDate;

    @SuppressLint("SimpleDateFormat")
    public static String DateTimeParseFormatter(String dateTime) {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate = new SimpleDateFormat("dd-MMM-yyyy h:mm a");
        try {
            dateTime = formatter_WorkDateDate.format(parseSimpleDateFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }
    public static String DateTimeParseMonthYearFormatter(String dateTime) {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate = new SimpleDateFormat("dd MMMM yyyy");
        try {
            dateTime = formatter_WorkDateDate.format(parseSimpleDateFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    public static String GetCurrentDate() {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String dateTime = parseSimpleDateFormat.format(date.getTime());
        return dateTime;
    }
    public static String GetCurrentTime() {
        long currentDateTime = System.currentTimeMillis();
        parseSimpleDateFormat = new SimpleDateFormat("h:mm a");
        String time = parseSimpleDateFormat.format(currentDateTime);
        return time;
    }

    public static String ConvertDateToTime(String dateTime) {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate = new SimpleDateFormat("h:mm a");
        try {
            dateTime = formatter_WorkDateDate.format(parseSimpleDateFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public static String CurrentOffsetDateTimeParser(String dateTime) {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        try {
            dateTime = formatter_WorkDateDate.format(parseSimpleDateFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String DateTimeParseMonthYearUTCFormatter(String dateTime) {
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        formatter_WorkDateDate = new SimpleDateFormat("dd MMMM yyyy");
        formatter_WorkDateDate.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
        try {
            dateTime = formatter_WorkDateDate.format(parseSimpleDateFormat.parse(dateTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public static String OffsetDateTimeParser(String dateTime) {
        try {
            long dv = Long.valueOf(dateTime) * 1000;// its need to be in milisecond
            Date df = new java.util.Date(dv);
            parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            parseSimpleDateFormat.setTimeZone(TimeZone.getTimeZone(ZoneOffset.UTC));
            dateTime = parseSimpleDateFormat.format(df);

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public static String OffsetTimeParser(String dateTime) {
        try {

            // Input format for UTC time
            SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the formatter to UTC

            // Parse the UTC time to a Date object
            Date date = utcFormatter.parse(dateTime);

            // Output format for local time
            SimpleDateFormat localFormatter = new SimpleDateFormat("h:mm a");

            // Set the formatter to the device's local timezone
            localFormatter.setTimeZone(TimeZone.getDefault()); // Get device's local timezone

            // Format the Date object to the device's local time
            dateTime = localFormatter.format(date);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public static String CurrentOffsetTimeParser(String dateTime) {
        // Input format for UTC time
        try {

            // Input format for UTC time
            SimpleDateFormat utcFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            utcFormatter.setTimeZone(TimeZone.getTimeZone("UTC")); // Set the formatter to UTC

            // Parse the UTC time to a Date object
            Date date = utcFormatter.parse(dateTime);

            // Output format for local time
            SimpleDateFormat localFormatter = new SimpleDateFormat("yyyy-MM-dd h:mm: a");

            // Set the formatter to the device's local timezone
            localFormatter.setTimeZone(TimeZone.getDefault()); // Get device's local timezone

            // Format the Date object to the device's local time
            dateTime = localFormatter.format(date);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SimpleDateFormat")
    public static String CurrentDateTimeParser() {
        String currentDateAndTime;
        parseSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault());
        currentDateAndTime = parseSimpleDateFormat.format(new Date());
        return currentDateAndTime;
    }
}
