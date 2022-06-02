package com.pedrocosta.utils.output;

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
        log(TRACE, obj, msg, null);
    }

    /**
     * Log debug messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void debug(Object obj, String msg) {
        log(DEBUG, obj, msg, null);
    }

    /**
     * Log information messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void info(Object obj, String msg) {
        log(INFO, obj, msg, null);
    }

    /**
     * Log warning messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void warn(Object obj, String msg) {
        log(WARN, obj, msg, null);
    }

    /**
     * Log error messages.
     *
     * @param obj   Context of the log.
     * @param msg   The message to log.
     */
    public static void error(Object obj, String msg) {
        error(obj, msg, null);
    }

    /**
     * Log error of a {@link Throwable}'s message.
     *
     * @param obj       Context of the log.
     * @param throwable {@link Throwable} object of error.
     */
    public static void error(Object obj, Throwable throwable) {
        error(obj, null, throwable);
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
        if (msg != null)
            log(ERROR, obj, msg, null);
        else if (throwable != null)
            log(ERROR, obj, null, throwable);
    }

    private static Logger getLogger(Object obj) {
        return LogManager.getLogger(obj);
    }

    private static void log(int type, Object obj, String msg, Throwable throwable) {
        switch(type) {
            case TRACE:
                getLogger(obj).trace(msg);
                break;
            case DEBUG:
                getLogger(obj).debug(msg);
                break;
            case INFO:
                getLogger(obj).info(msg);
                break;
            case WARN:
                getLogger(obj).warn(msg);
                break;
            case ERROR:
                if (throwable == null)
                    getLogger(obj).error(msg);
                else
                    getLogger(obj).error(obj, throwable);
                break;
            default:
                throw new IllegalArgumentException("Wrong log type");
        }
    }
}
