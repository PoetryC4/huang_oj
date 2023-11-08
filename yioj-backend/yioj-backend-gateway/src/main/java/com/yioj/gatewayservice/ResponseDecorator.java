package com.yioj.gatewayservice;

import cn.hutool.json.JSONObject;
import com.yioj.common.utils.JwtUtils;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import java.nio.charset.Charset;

/**
 * @desc 响应装饰器（重构响应体）
 */
public class ResponseDecorator extends ServerHttpResponseDecorator{
    private String newJwt;

    public void setNewJwt(String newJwt) {
        this.newJwt = newJwt;
    }

    public String getNewJwt() {
        return newJwt;
    }

    public ResponseDecorator(ServerHttpResponse delegate){
        super(delegate);
    }

    @Override
    @SuppressWarnings(value = "unchecked")
    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {

        if(body instanceof Flux) {
            Flux<DataBuffer> fluxBody = (Flux<DataBuffer>) body;

            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {
                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                DataBuffer join = dataBufferFactory.join(dataBuffers);

                byte[] content = new byte[join.readableByteCount()];
                join.read(content);
                DataBufferUtils.release(join);// 释放掉内存

                String bodyStr = new String(content, Charset.forName("UTF-8"));

                //修改响应体
                bodyStr = modifyBody(bodyStr);

                getDelegate().getHeaders().setContentLength(bodyStr.getBytes().length);
                return bufferFactory().wrap(bodyStr.getBytes());
            }));
        }
        return super.writeWith(body);
    }

    //重写这个函数即可
    private String modifyBody(String jsonStr){
        JSONObject json = new JSONObject(jsonStr);
        // 修改响应体
        json.set(JwtUtils.JWT_HEADER_KEY, newJwt);

        return json.toString();
    }
}

