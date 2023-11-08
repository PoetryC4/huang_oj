package com.yioj.gatewayservice.config;

import com.yioj.common.utils.JwtUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.reactive.HiddenHttpMethodFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * 全局跨域配置
 *
 */
@Configuration
public class CorsConfig{

    @Bean
    public CorsWebFilter corsWebFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //1.配置跨域
        //允许哪种请求头跨域
        corsConfiguration.addAllowedHeader("*");
        //允许哪种方法类型跨域 get post delete put
        corsConfiguration.addAllowedMethod("*");
        // 允许哪些请求源跨域
        // TODO 分布式跨域
        corsConfiguration.addAllowedOriginPattern("*");
        // 是否携带cookie跨域
        corsConfiguration.setAllowCredentials(true);

        // 暴露响应头
        corsConfiguration.addExposedHeader(JwtUtils.JWT_HEADER_KEY);

        //允许跨域的路径
        source.registerCorsConfiguration("/**",corsConfiguration);
        return new CorsWebFilter(source);
    }

    /**
     * 解决SpringCloud处理Get以外请求403的错误
     * @return
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter() {
            @NotNull
            @Override
            public Mono<Void> filter(@NotNull ServerWebExchange exchange, @NotNull WebFilterChain chain) {
                return chain.filter(exchange);
            }
        };
    }
}
