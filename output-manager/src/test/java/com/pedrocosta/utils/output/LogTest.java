package com.pedrocosta.utils.output;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Scanner;

public class LogTest {

    private String logFilePath;
    private File logFile;

    @BeforeEach
    public void setUp() {
        logFilePath = "logs\\test.log";
    }

    @AfterEach
    public void tearDown() throws IOException {
        new FileWriter(logFile, false).close();
        logFile = null;
    }

    @Test
    @Order(0)
    public void testLogFileCreation() {
        Log.trace(this, "test creation");
        logFile = new File(logFilePath);
        assert logFile.exists();
    }

    @Test
    @Order(1)
    public void testInfoLog() throws FileNotFoundException {
        String msg = "Test of info log.";
        Log.info(this, msg);
        logFile = new File(logFilePath);
        assert logHasMessage(msg);
    }

    @Test
    @Order(1)
    public void testWarnLog() throws FileNotFoundException {
        String msg = "Test of warning log.";
        Log.warn(this, msg);
        logFile = new File(logFilePath);
        assert logHasMessage(msg);
    }

    @Test
    @Order(1)
    public void testErrorLog() throws FileNotFoundException {
        String msg = "Test of error log.";
        Log.error(this, "Test of error log.");
        logFile = new File(logFilePath);
        assert logHasMessage(msg);
    }

    @Test
    @Order(1)
    public void testErrorLogWithException() throws FileNotFoundException {
        Log.error(this, new NullPointerException("Test of error log."));
        logFile = new File(logFilePath);
        assert logHasMessage("java.lang.NullPointerException: Test of error log.");
    }

    @Test
    @Order(1)
    public void testDebugLog() throws FileNotFoundException {
        String msg = "Test of debug log.";
        Log.debug(this, "Test of debug log.");
        logFile = new File(logFilePath);
        assert logHasMessage(msg);
    }

    private boolean logHasMessage(String msg) throws FileNotFoundException {
        if (logFile == null) {
            return false;
        }
        boolean result = false;
        Scanner reader = new Scanner(logFile);
        while (reader.hasNextLine()) {
            String data = reader.nextLine();
            result = data.contains(msg);
            if (result) {
                break;
            }
        }
        reader.close();
        return result;
    }

    private boolean logHasMessage2(String msg) throws IOException {
        if (logFile == null) {
            return false;
        }
        boolean result = false;
        FileReader freader = new FileReader(logFile);
        BufferedReader bfreader = null;
        try {
            bfreader = new BufferedReader(freader);
            String line;
            while ((line = bfreader.readLine()) != null) {
                result = line.contains(msg);
                if (result) break;
            }
        }
        finally {
            if (bfreader != null) {
                bfreader.close();
            }
            freader.close();
        }
        return result;
    }
}
