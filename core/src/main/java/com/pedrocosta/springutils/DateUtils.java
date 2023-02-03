package com.pedrocosta.springutils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Util class to manage dates.
 *
 * @author Pedro H. M. da Costa
 * @version 1.0
 */
public class DateUtils {

    public static final String DEFAULT_FORMAT = "yyyy-MM-dd";
    public static final String S_DAY = "DAY";
    public static final String S_MONTH = "MONTH";
    public static final String S_YEAR = "YEAR";

    /**
     * Convert date from {@link String} to {@link Date} object. <br>
     * Default format: 'yyyy-MM-dd'.
     *
     * @param s date on String to convert
     * @return {@link Date} object of date.
     */
    public static Date stringToDate(final String s) {
        return stringToDate(s, DEFAULT_FORMAT);
    }

    /**
     * Convert date from {@link String} to {@link Date} object.
     *
     * @param s         date on String to convert
     * @param format    format schema
     * @return {@link Date} object of date.
     */
    public static Date stringToDate(final String s, final String format) {
        if (s == null) return null;
        Date date = null;
        try {
            date = new SimpleDateFormat(format).parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * Convert date from {@link Date} to {@link String} object.<br>
     *  Default format: 'yyyy-MM-dd'.
     *
     * @param d date on {@link Date} to convert
     * @return {@link String} object of date.
     */
    public static String dateToString(final Date d) {
        return dateToString(d, DEFAULT_FORMAT);
    }

    /**
     * Convert date from {@link Date} to {@link String} object.
     *
     * @param d         date on {@link Date} to convert
     * @param format    format schema
     * @return {@link String} object of date.
     */
    public static String dateToString(final Date d, final String format) {
        if (d == null) return null;
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * Add a day to specific date.
     *
     * @param date  Original date
     * @param n     Number of days to add
     * @return New date after addition.
     */
    public static Date addDay(@NotNull final Date date, final int n) {
        return add(date, Calendar.DATE, n);
    }

    /**
     * Add a month to specific date.
     *
     * @param date  Original date
     * @param n     Number of months to add
     * @return New date after addition.
     */
    public static Date addMonth(@NotNull final Date date, final int n) {
        return add(date, Calendar.MONTH, n);
    }

    /**
     * Add a year to specific date.
     *
     * @param date  Original date
     * @param n     Number of years to add
     * @return New date after addition.
     */
    public static Date addYear(@NotNull final Date date, final int n) {
        return add(date, Calendar.YEAR, n);
    }

    /**
     * Add days, months or years to specific date.
     *
     * @param date  Original date
     * @param type  Type of period to add (day, month or year)
     * @param n     Number of days, months or years
     * @return New date after addition.
     */
    @Nullable
    public static Date addPeriod(@NotNull final Date date, final String type, final int n) {
        switch (type) {
            case DateUtils.S_DAY:
                return DateUtils.addDay(date, n);
            case DateUtils.S_MONTH:
                return DateUtils.addMonth(date, n);
            case DateUtils.S_YEAR:
                return DateUtils.addYear(date, n);
            default:
                return null;
        }
    }

    private static Date add(final Date date, final int type, final int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(type, n);
        return cal.getTime();
    }
}
