package com.yioj.gatewayservice.filter;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.yioj.common.utils.JwtUtils;
import com.yioj.gatewayservice.ResponseDecorator;
import com.yioj.model.model.entity.User;
import com.yioj.model.model.enums.UserRoleEnum;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * * JWT验证过滤器，配置顺序 ：CorsFilter-->JwtFilter-->struts2中央控制器
 *
 * @author Administrator
 */
@Component
@Slf4j
public class GlobalJwtFilter implements GlobalFilter, Ordered {
    //路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    private boolean OFF = false;// true关闭jwt令牌验证功能
    private static String[] urlsForbbiden = new String[]{
            "/**/inner/**",
    };
    private static String[] urlsDefault = new String[]{
            "**/api/user/login**",
    };
    /*@Resource
    private UserFeignClient userFeignClient;*/


    /**
     * 验证jwt令牌，验证通过返回声明(包括公有和私有)，返回null则表示验证失败
     */
    private Claims validateJwtToken(String jwt) {
        Claims claims = null;
        try {
            if (null != jwt) {
                //该解析方法会验证：1）是否过期 2）签名是否成功
                claims = JwtUtils.parseJwt(jwt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * 是否为排除的URL
     *
     * @param path
     * @return
     */
    private boolean checkDefault(String path) {
        for (String url : urlsDefault) {
            boolean match = PATH_MATCHER.match(url, path);
            if (match) {
                return true;
            }
        }
        return false;
    }

    /**
     * 路径匹配，检查本次请求是否需要放行
     *
     * @param requestURI
     * @return
     */
    public boolean checkForbbiden(String requestURI) {
        for (String url : urlsForbbiden) {
            boolean match = PATH_MATCHER.match(url, requestURI);
            if (match) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        log.info("拦截到请求:{}", path);

        if (checkDefault(path)) {
            return chain.filter(exchange);
        } else if (checkForbbiden(path)) {
            ServerHttpResponse response = exchange.getResponse();
            HttpHeaders headers = request.getHeaders();
            List<String> strings = headers.get(JwtUtils.JWT_HEADER_KEY);
            if (strings == null || strings.size() == 0) {
                log.info("无jwt");
                response.setStatusCode(HttpStatus.FORBIDDEN);
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                DataBuffer wrap = dataBufferFactory.wrap("No jwt".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(wrap));
            }
            Claims claims = this.validateJwtToken(strings.get(0));
            if (null == claims) {
                // resp.setCharacterEncoding("UTF-8");
                response.setStatusCode(HttpStatus.FORBIDDEN);
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                DataBuffer wrap = dataBufferFactory.wrap("JWT expired".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(wrap));
            }
            if (!claims.containsKey("id")) {
                log.info("无jwt");
                response.setStatusCode(HttpStatus.FORBIDDEN);
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                DataBuffer wrap = dataBufferFactory.wrap("No jwt".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(wrap));
            }
            Long userId = (Long) claims.get("id");
            // User user = userFeignClient.getById(userId);

            // TODO 尝试在Filter中获取Bean
            String responseStr = HttpUtil.createGet("http://127.0.0.1:8002/api/user/inner/get/id?userId=" + userId)
                    .execute()
                    .body();
            User user = JSON.parseObject(responseStr, User.class);
            //从声明部分中获取私有声明
            //获取私有声明中的User对象 -> Modules

            if (user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole())) {
                String newJwt = JwtUtils.copyJwt(strings.get(0), JwtUtils.JWT_WEB_TTL);
                ResponseDecorator decorator = new ResponseDecorator(exchange.getResponse());
                decorator.setNewJwt(newJwt);
                return chain.filter(exchange.mutate().response(decorator).build());
            } else {
                DataBufferFactory dataBufferFactory = response.bufferFactory();
                DataBuffer wrap = dataBufferFactory.wrap("No auth".getBytes(StandardCharsets.UTF_8));
                return response.writeWith(Mono.just(wrap));
            }
        } else {
            return chain.filter(exchange);
        }
    }
}
