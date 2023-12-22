package com.waterbucket.chatroomAPI.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RetryService {
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private static final long RETRY_DELAY_MS = 200;

    public <T> T executeWithRetry(RetryOperation<T> operation) {
        for (int i = 0; i < MAX_RETRY_ATTEMPTS; ) {
            try {
                return operation.execute();
            } catch (Exception e) {
                try {
                    Thread.sleep(RETRY_DELAY_MS);
                } catch (InterruptedException e1) {
                    log.error("Thread sleep interrupted");
                    throw new RuntimeException(e1);
                }
                log.info("Exception occurred. Retry attempt: {}", i);
                ++i;
            }
        }
        throw new RuntimeException("Max retry attempts reached");
    }

    public interface RetryOperation<T> {
        T execute() throws Exception;
    }
}
