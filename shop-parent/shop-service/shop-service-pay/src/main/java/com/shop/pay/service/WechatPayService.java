package com.shop.pay.service;

import java.util.Map;

public interface WechatPayService {

    /***
     * create QR code from wechat
     * @param parameterMap
     * @return
     */
    Map createNativeQR(Map<String, String> parameterMap);

    /****
     * query status of payment from wechat
     * @param outTradeNo
     * @return
     */
    Map queryStatus(String outTradeNo);

    /***
     * close order in wechat
     * @param outTradeNo
     * @return
     */
    Map closeOrder(String outTradeNo);
}
