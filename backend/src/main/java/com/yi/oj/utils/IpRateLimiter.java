package com.yi.oj.utils;

import com.google.common.util.concurrent.RateLimiter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class IpRateLimiter {
    private final Map<String, RateLimiter> rateLimiters = new ConcurrentHashMap<>();
    private final double requestsPerSecond;

    public IpRateLimiter(double requestsPerSecond) {
        this.requestsPerSecond = requestsPerSecond;
    }

    public boolean allowRequest(String ip) {
        // 1秒刷新
        RateLimiter rateLimiter = rateLimiters.computeIfAbsent(ip, k -> RateLimiter.create(requestsPerSecond));
        return rateLimiter.tryAcquire();
    }
}