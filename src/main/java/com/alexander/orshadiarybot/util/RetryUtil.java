package com.alexander.orshadiarybot.util;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
public class RetryUtil {

    public static <T> T tryAndGetOrElseThrow(int retryCount, long delay, Supplier<T> supplier, Supplier<RuntimeException> exceptionSupplier) {
        if (retryCount <= 0) {
            throw new IllegalArgumentException("retryCount must be greater than 0");
        }
        Exception lastException = null;
        while (retryCount > 0) {
            try {
                return supplier.get();
            } catch (Exception e) {
                retryCount--;
                log.info("Exception occurred while trying to get data from the source. {} attempts left", retryCount);
                lastException = e;
            }
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                log.warn("Interrupted while waiting for retry: {}", e.getMessage());
            }
        }
        RuntimeException runtimeException = exceptionSupplier.get();
        runtimeException.addSuppressed(lastException);
        throw runtimeException;
    }
}
