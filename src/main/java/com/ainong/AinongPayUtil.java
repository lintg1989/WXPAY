package com.ainong;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jonechen on 2017/3/28.
 */
public class AinongPayUtil {

    private static Log logger = LogFactory.getLog(AinongPayUtil.class);

    static IHttpRequestHelper httpRequestHelper = new HttpRequestPayHelper("http://pay.chinagpay.com/bas/BgTrans");

    public static Map<String, String> dopayAinong(Map<String, String> postargs)
            throws ServletException, IOException {

        Map<String, String> apiResult = null;
        Map<String, String> resMap = new HashMap<String, String>();

        try {
            Map<String, String> paras = new HashMap<String, String>();
            paras.put("version", "1.0.0");// 消息版本号
            paras.put("txnType", "41");// 微信--41
            paras.put("txnSubType", "00");// 交易子类型
            paras.put("bizType", "000000");// 产品类型
            paras.put("accessType", "0");// 接入类型
            paras.put("accessMode", "01");// 接入方式
            paras.put("payType", "0704");// 公众号支付--0704
            paras.put("merId", "929010048160219");// 商户号


            paras.put("merOrderId", new Date().getTime()+"");// 商户订单号
            paras.put("appId", "wx0f5cc2bcb9b35ef8");// 微信公众平台id
            paras.put("subOpenId", postargs.get("subOpenId"));// 商户微信公众号里边给用户分配的id
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            paras.put("txnTime", simpleDateFormat.format(new Date()));// 订单发送时间
            paras.put("txnAmt", "1");// 交易金额（分）
            paras.put("currency", "CNY");// 交易币种
            paras.put("backUrl", "http://116.62.25.89/onlinePay/callbackAN.htm");// 后台通知地址
            paras.put("payTimeOut", "");// 支付超出时间
            paras.put("subject", "ceshi");// 商品标题
            paras.put("body", "test");// 商品描述
            paras.put("customerIp", "127.0.0.1");// 商户终端ip
            paras.put("merResv1", "");// 请求保留域

            apiResult = httpRequestHelper.sendPost(null, paras, "UTF-8", 1);

            if (apiResult == null) {
                resMap.put("respCode", "7777");
                resMap.put("respMsg", "系统异常");//系统异常
                //记录支付服务返回

            }else{
                //记录支付服务返回
                resMap.put("respCode", apiResult.get("respCode"));
                resMap.put("respMsg", apiResult.get("respMsg"));

            }
        }catch(Exception e){
            resMap.put("respCode", "7777");
            resMap.put("respMsg", "系统异常");//系统异常
            System.out.println("系统异常公众号支付调用失败！");
        }

        return  apiResult;

    }


    public static String getResv(Map<String, String> requestMap)throws Exception{
        Map<String, String> relMap = dopayAinong(requestMap);
        return relMap.get("resv");
    }

}
