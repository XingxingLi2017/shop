package com.shop.pay.controller;

import com.alibaba.fastjson.JSON;
import com.github.wxpay.sdk.WXPayUtil;
import com.shop.entity.Result;
import com.shop.entity.StatusCode;
import com.shop.pay.service.WechatPayService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@RestController
@RequestMapping("/wechat/pay")
@CrossOrigin
public class WechatPayController {

    @Autowired
    private WechatPayService wechatPayService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${mq.pay.exchange.order}")
    String orderExchange;

    @Value("${mq.pay.queue.order}")
    String orderQueue;

    @Value("${mq.pay.routing.key}")
    String routingKey;

    @RequestMapping("/notify/url")
    public String notifyUrl(HttpServletRequest request) throws Exception {

        // read response of wechat callback
        ServletInputStream is = request.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] buffer = new byte[1024];
        while((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }

        String xmlResult = new String(baos.toByteArray(), "UTF-8");
        Map<String, String> map = WXPayUtil.xmlToMap(xmlResult);
        System.out.println(map);

        // send payment result to MQ
        rabbitTemplate.convertAndSend(orderExchange, routingKey, JSON.toJSONString(map));

        // send ack code to wechat
        String result = "<xml>\n" +
                "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                "</xml>";

        return result;
    }


    /***
     * query payment status from wechat
     * @param outTradeNo
     * @return
     */
    @GetMapping("/status/query")
    public Result queryStatus(@RequestParam("outTradeNo") String outTradeNo) {
        Map map = wechatPayService.queryStatus(outTradeNo);
        return new Result(true, StatusCode.OK, "Query status successfully.", map);
    }

    /****
     * create QR code
     */
    @GetMapping("/create/native")
    public Result createNative(@RequestParam Map<String, String> paramMap) {
        Map resultMap = wechatPayService.createNativeQR(paramMap);
        return new Result(true, StatusCode.OK, "Create QR code successfully.", resultMap);
    }

}
