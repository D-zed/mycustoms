package com.jinaup.upcustoms.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jinaup.common.ResultCommon;
import com.jinaup.pojo.customs.CustomInfo;
import com.jinaup.service.coop.customsinfoservice.CustomInfoService;
import com.jinaup.upcustoms.pojo.CustomInfo;
import com.jinaup.upcustoms.service.RealTimeDataUpload;
import com.jinaup.upcustoms.websocket.WebSocketClient1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.Session;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/7
 */
@RestController
public class SignController2 {


    @Autowired
    private RealTimeDataUpload realTimeDataUpload;








    //ok  就用这个了对数据进行加签   并且接收的参数只有一个初始化数据即可
    @PostMapping("signnew2")
    public String signNew2(String initData,String customInfo){


        String sendArgs = "{\"_method\":\"cus-sec_SpcSignDataAsPEM\",\"_id\":1,\"args\":{\"inData\":\"" + initData.replace("\"","\\\"") + "\",\"passwd\":\"88888888\"}}";
        JSONObject jsonObject = JSON.parseObject(customInfo);
        CustomInfo customInfo1 = (CustomInfo) JSON.toJavaObject(jsonObject, CustomInfo.class);
        //将数据加入供上传数据的拼接使用
        WebSocketClient1.setCustomInfo(customInfo1);
        WebSocketClient1.setRealTimeDataUpload(realTimeDataUpload);
        //建立连接
        WebSocketClient1.inticonn();
        Session session = WebSocketClient1.sessionMap.get("1");
        //通过websocket进行加签
        WebSocketClient1.sendMsgToServer(session,sendArgs);
        //这个地方返回的是
       return "aaa";
    }


   /* //ok  就用这个了对数据进行加签 （测试用）  并且接收的参数只有一个初始化数据即可
    @PostMapping("signnew")
    public String signNew(String inidata){

        String orderNo="20190411230410641618";
        String recpAccount="2201080509100187672";
        StringBuilder initData = new StringBuilder();
        //以下拼接代码放到定时任务中进行拼接
        initData.append("\"sessionID\":\"fe2374-8fnejf97-55616242\"||\"payExchangeInfoHead\":\"{\"guid\":\"9D55BA71-55DE-41F4-8B50-C36C83B3B419\",\"initalRequest\":\"https://openapi.alipay.com/gateway.do?timestamp=2013-01-0108:08:08&method=alipay.trade.pay&app_id=13580&sign_type=RSA2&sign=ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE&version=1.0&charset=GBK\",\"initalResponse\":\"ok\",\"ebpCode\":\"461216012c\",\"payCode\":\"312226T001\",\"payTransactionId\":\"2018121222001354081010726129\",\"totalAmount\":100,\"currency\":\"142\",\"verDept\":\"3\",\"payType\":\"1\",\"tradingTime\":\"20181212041803\",\"note\":\"批量订单，测试订单优化,生成多个so订单\"}\"||\"payExchangeInfoLists\":\"[{\"orderNo\":\"SO1710301150602574003\",\"goodsInfo\":[{\"gname\":\"lhy-gnsku3\",\"itemLink\":\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453\"},{\"gname\":\"lhy-gnsku2\",\"itemLink\":\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453\"}],\"recpAccount\":\""+recpAccount+"\",\"recpCode\":\"\",\"recpName\":\"YUNJIHONGKONGLIMITED\"}]\"||\"serviceTime\":\"1544519952469\"");
        String initData0 = initData.toString();

        String sendArgs = "{\"_method\":\"cus-sec_SpcSignDataAsPEM\",\"_id\":1,\"args\":{\"inData\":\"" + initData0.replace("\"","\\\"") + "\",\"passwd\":\"88888888\"}}";

        // String sendArg="{\"_method\":\"cus-sec_SpcSignDataAsPEM\",\"_id\":1,\"args\":{\"inData\":\"\\\"sessionID\\\":\\\"1dd44ea3-202a-43e7-87a5-6d82ca3fce4f\\\"||\\\"payExchangeInfoHead\\\":\\\"{\\\"guid\\\":\\\"9D55BA71-55DE-41F4-8B50-C36C83B3B419\\\",\\\"initalRequest\\\":\\\"https://openapi.alipay.com/gateway.do?timestamp=2013-01-0108:08:08&method=alipay.trade.pay&app_id=13580&sign_type=RSA2&sign=ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE&version=1.0&charset=GBK\\\",\\\"initalResponse\\\":\\\"ok\\\",\\\"ebpCode\\\":\\\"xxxxxxxx\\\",\\\"payCode\\\":\\\"312226T001\\\",\\\"payTransactionId\\\":\\\"2018121222001354081010726129\\\",\\\"totalAmount\\\":100,\\\"currency\\\":\\\"142\\\",\\\"verDept\\\":\\\"3\\\",\\\"payType\\\":\\\"1\\\",\\\"tradingTime\\\":\\\"20181212041803\\\",\\\"note\\\":\\\"批量订单，测试订单优化,生成多个so订单\\\"}\\\"||\\\"payExchangeInfoLists\\\":\\\"[{\\\"orderNo\\\":\\\"SO1710301150602574003\\\",\\\"goodsInfo\\\":[{\\\"gname\\\":\\\"lhy-gnsku3\\\",\\\"itemLink\\\":\\\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999761&shopId=453\\\"},{\\\"gname\\\":\\\"lhy-gnsku2\\\",\\\"itemLink\\\":\\\"http://m.yunjiweidian.com/yunjibuyer/static/vue-buyer/idc/index.html#/detail?itemId=999760&shopId=453\\\"}],\\\"recpAccount\\\":\\\"OSA571908863132601\\\",\\\"recpCode\\\":\\\"\\\",\\\"recpName\\\":\\\"YUNJIHONGKONGLIMITED\\\"}]\\\"||\\\"serviceTime\\\":\\\"1544519952469\\\"\",\"passwd\":\"88888888\"}}";


        //建立连接
        WebSocketClient1.inticonn();
        Session session = WebSocketClient1.sessionMap.get("1");
        //加签的原始数据
        WebSocketClient1.sendMsgToServer(session,sendArgs);
        return "aaa";
    }*/
}


