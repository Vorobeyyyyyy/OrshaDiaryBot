package com.alexander.orshadiarybot.util;

import java.util.function.Supplier;

public class RetryUtil {

    public static <T> T tryAndGetOrElseThrow(int repeatCount, long delay, Supplier<T> supplier, Supplier<RuntimeException> exceptionSupplier) {
        StackTraceElement[] stackTrace = new StackTraceElement[0];
        while (repeatCount > 0) {
            try {
                return supplier.get();
            } catch (Exception e) {
                repeatCount--;
                stackTrace = e.getStackTrace();
            }
        }
        RuntimeException runtimeException = exceptionSupplier.get();
        runtimeException.setStackTrace(stackTrace);
        throw runtimeException;
    }
}
