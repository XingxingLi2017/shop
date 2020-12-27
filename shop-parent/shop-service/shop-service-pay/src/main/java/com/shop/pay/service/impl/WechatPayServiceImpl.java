package com.shop.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.shop.pay.service.WechatPayService;
import com.shop.util.HttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class WechatPayServiceImpl implements WechatPayService {

    @Value("${wechat.appid}")
    private String appid;

    @Value("${wechat.partner}")
    private String partner;

    @Value("${wechat.partnerkey}")
    private String partnerkey;

    @Value("${wechat.notifyurl}")
    private String notifyurl;

    /***
     * get QR code
     */
    @Override
    public Map createNativeQR(Map<String, String> parameterMap) {
        // prepare params
        Map<String, String> map = new HashMap<>();

        map.put("appid", appid);
        map.put("mch_id", partner); // merchant id
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("body", "shop wechat payment");
        map.put("out_trade_no", parameterMap.get("outTradeNo"));    // order number
        map.put("total_fee", parameterMap.get("totalFee"));     // cent unit
        map.put("spbill_create_ip", "127.0.0.1");   // ip address for pos machine
        map.put("notify_url", notifyurl);
        map.put("trade_type", "NATIVE");

        // get attach data: MQ exchange name a queue name
        Map<String, String> attachMap = new HashMap<>();
        attachMap.put("exchange", parameterMap.get("exchange"));
        attachMap.put("routingKey", parameterMap.get("routingKey"));
        // seckill order needs username
        String username = parameterMap.get("username");
        if(username != null) {
            attachMap.put("username", username);
        }

        String attach = JSON.toJSONString(attachMap);
        map.put("attach", attach);

        try {
            // signature
            String xmlParameters = WXPayUtil.generateSignedXml(map, partnerkey);

            // url
            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParameters);
            httpClient.post();

            // get result
            String result = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    @Override
    public Map queryStatus(String outTradeNo) {

        // prepare params
        Map<String, String> map = new HashMap<>();

        map.put("appid", appid);
        map.put("mch_id", partner); // merchant id
        map.put("nonce_str", WXPayUtil.generateNonceStr());
        map.put("out_trade_no",outTradeNo);    // order number
        try {
            // signature
            String xmlParameters = WXPayUtil.generateSignedXml(map, partnerkey);

            // url
            String url = "https://api.mch.weixin.qq.com/pay/orderquery";
            HttpClient httpClient = new HttpClient(url);
            httpClient.setHttps(true);
            httpClient.setXmlParam(xmlParameters);
            httpClient.post();

            // get result
            String result = httpClient.getContent();
            Map<String, String> resultMap = WXPayUtil.xmlToMap(result);

            return resultMap;

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
