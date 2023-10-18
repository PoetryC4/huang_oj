package com.huang.oj.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

/**
 * 网络工具类
 *
 * @author <a href="https://github.com/liyupi">程序员鱼皮</a>
 * @from <a href="https://yupi.icu">编程导航知识星球</a>
 */
public class NetUtils {

    /**
     * 获取客户端 IP 地址
     *
     * @param request
     * @return
     */
    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if (ip.equals("127.0.0.1")) {
                // 根据网卡取本机配置的 IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (inet != null) {
                    ip = inet.getHostAddress();
                }
            }
        }
        // 多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ip != null && ip.length() > 15) {
            if (ip.indexOf(",") > 0) {
                ip = ip.substring(0, ip.indexOf(","));
            }
        }
        if (ip == null) {
            return "127.0.0.1";
        }
        return ip;
    }

    public static String RequestWithBody(String protocol, String host, String port, String path, Object requestBody) throws Exception {

        URL url = new URL(protocol + "://" + host + ":" + port + "/" + path);
        // 打开和URL之间的连接
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");//请求post方式
        con.setUseCaches(false); // Post请求不能使用缓存
        con.setDoInput(true);// 设置是否从HttpURLConnection输入，默认值为 true
        con.setDoOutput(true);// 设置是否使用HttpURLConnection进行输出，默认值为 false

        //设置header内的参数 connection.setRequestProperty("健, "值");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("isTree", "true");
        con.setRequestProperty("isLastPage", "true");

        // 建立实际的连接
        con.connect();

        // 得到请求的输出流对象
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream(), "UTF-8");
        writer.write(com.alibaba.fastjson2.JSON.toJSONString(requestBody));
        writer.flush();

        // 获取服务端响应，通过输入流来读取URL的响应
        InputStream is = con.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        StringBuffer sbf = new StringBuffer();
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();

        // 关闭连接
        con.disconnect();

        return sbf.toString();

    }

}
