package com.huang.oj.config;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.core.lang.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * 熔断
 */
@Configuration
public class CircuitBreakerConfiguration {

    @Bean
    public CircuitBreaker circuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(50) // 失败率阈值，触发熔断
                .waitDurationInOpenState(Duration.ofMillis(10000)) // 在熔断状态下等待时间
                .ringBufferSizeInHalfOpenState(10) // 半开状态下的环形缓冲区大小
                .ringBufferSizeInClosedState(100) // 关闭状态下的环形缓冲区大小
                .build();

        return CircuitBreaker.of("problemCircuitBreaker", config);
    }
}
