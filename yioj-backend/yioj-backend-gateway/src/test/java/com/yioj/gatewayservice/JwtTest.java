package com.yioj.gatewayservice;

import com.yioj.common.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class JwtTest {

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    @Test
    public void test1() {// 生成JWT
        //JWT Token=Header.Payload.Signature
        //头部.载荷.签名
        //Payload=标准声明+私有声明+公有声明

        //定义私有声明
        Map<String, Object> claims = new HashMap<>();
        claims.put("userName", "用户");
        claims.put("userAccount", "hzy777");
        claims.put("userEmail", "clouduwud@gmail.com");

        //TTL:Time To Live
        String jwt = JwtUtils.createJwt(claims, JwtUtils.JWT_WEB_TTL);
        System.out.println(jwt);
        //获取Payload（包含标准和私有声明）
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Claims parseJwt = JwtUtils.parseJwt(jwt);
        for (Map.Entry<String, Object> entry : parseJwt.entrySet()) {
            System.out.println("jjj");
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
        Date d1 = parseJwt.getIssuedAt();
        Date d2 = parseJwt.getExpiration();
        System.out.println("令牌签发时间：" + sdf.format(d1));
        System.out.println("令牌过期时间：" + sdf.format(d2));
    }
}
