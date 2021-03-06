package com.pedrocosta.springutils.output;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class used to log system messages.
 *
 * @author Pedro H M da Costa
 * @version 1.0
 */
public final class Log {

    private static final int TRACE = 0;
    private static final int DEBUG = 1;
    private static final int INFO = 2;
    private static final int WARN = 3;
    private static final int ERROR = 4;

    /**
     * Log trace messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void trace(Object obj, String msg) {
        trace(obj.getClass(), msg);
    }

    /**
     * Log trace messages.
     *
     * @param clazz Context of the log.
     * @param msg   The message to log.
     */
    public static void trace(Class<?> clazz, String msg) {
        log(TRACE, clazz, msg, null);
    }

    /**
     * Log debug messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void debug(Object obj, String msg) {
        debug(obj.getClass(), msg);
    }

    /**
     * Log debug messages.
     *
     * @param clazz Context of the log.
     * @param msg   The message to log.
     */
    public static void debug(Class<?> clazz, String msg) {
        log(DEBUG, clazz, msg, null);
    }

    /**
     * Log information messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void info(Object obj, String msg) {
        info(obj.getClass(), msg);
    }

    /**
     * Log information messages.
     *
     * @param clazz Context of the log.
     * @param msg   The message to log.
     */
    public static void info(Class<?> clazz, String msg) {
        log(INFO, clazz, msg, null);
    }

    /**
     * Log warning messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void warn(Object obj, String msg) {
        warn(obj.getClass(), msg);
    }

    /**
     * Log warning messages.
     *
     * @param clazz Context of the log.
     * @param msg   The message to log.
     */
    public static void warn(Class<?> clazz, String msg) {
        log(WARN, clazz, msg, null);
    }

    /**
     * Log error messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void error(Object obj, String msg) {
        error(obj.getClass(), msg);
    }

    /**
     * Log error messages.
     *
     * @param clazz Context of the log.
     * @param msg   The message to log.
     */
    public static void error(Class<?> clazz, String msg) {
        error(clazz, msg, null);
    }

    /**
     * Log error of a {@link Throwable}'s message.
     *
     * @param obj       Context of the log.
     * @param throwable {@link Throwable} object of error.
     */
    public static void error(Object obj, Throwable throwable) {
        error(obj.getClass(), throwable);
    }

    /**
     * Log error of a {@link Throwable}'s message.
     *
     * @param clazz     Context of the log.
     * @param throwable {@link Throwable} object of error.
     */
    public static void error(Class<?> clazz, Throwable throwable) {
        error(clazz, null, throwable);
    }

    /**
     * Log error with custom message or {@link Throwable}'s message.
     * If both custom message and {@link Throwable} object are set,
     * it will prioritize custom message.
     *
     * @param obj       Context of the log.
     * @param msg       The message to log.
     * @param throwable {@link Throwable} object of error.
     */
    public static void error(Object obj, String msg, Throwable throwable) {
        error(obj.getClass(), msg, throwable);
    }

    /**
     * Log error with custom message or {@link Throwable}'s message.
     * If both custom message and {@link Throwable} object are set,
     * it will prioritize custom message.
     *
     * @param clazz     Context of the log.
     * @param msg       The message to log.
     * @param throwable {@link Throwable} object of error.
     */
    public static void error(Class<?> clazz, String msg, Throwable throwable) {
        if (msg != null)
            log(ERROR, clazz, msg, null);
        else if (throwable != null)
            log(ERROR, clazz, null, throwable);
    }

    private static Logger getLogger(String name) {
        return LogManager.getLogger(name);
    }

    private static void log(int type, Object obj, String msg, Throwable throwable) {
        log(type, obj.getClass().getSimpleName(), msg, throwable);
    }

    private static void log(int type, Class<?> clazz, String msg, Throwable throwable) {
        log(type, clazz.getSimpleName(), msg, throwable);
    }

    private static void log(int type, String name, String msg, Throwable throwable) {
        switch(type) {
            case TRACE:
                getLogger(name).trace(msg);
                break;
            case DEBUG:
                getLogger(name).debug(msg);
                break;
            case INFO:
                getLogger(name).info(msg);
                break;
            case WARN:
                getLogger(name).warn(msg);
                break;
            case ERROR:
                if (throwable == null)
                    getLogger(name).error(msg);
                else
                    getLogger(name).error(name, throwable);
                break;
            default:
                throw new IllegalArgumentException("Wrong log type");
        }
    }
}
