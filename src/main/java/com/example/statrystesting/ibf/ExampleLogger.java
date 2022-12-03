package com.example.statrystesting.ibf;

import java.util.logging.Level;

public class ExampleLogger {
    private static ExampleLogger instance = new ExampleLogger();

    public static ExampleLogger getMainLogger() {
        return instance;
    }

    public void info(String starting) {
        System.out.println("INFO:" + starting);
    }

    public void log(Level severe, String s, Throwable t) {
        System.out.println(severe.getName() + ":" + s);
    }

    public void severe(String s) {

    }

    public void warning(String message) {

    }

    public void notice(String s) {

    }

    public void customerWarning(Object truncate_ddl) {

    }

    public void customerInfo(Object ddl) {

    }
}
