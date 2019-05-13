package com.jinaup.upcustoms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jinaup.upcustoms.config.WebSocketConfig;
import com.jinaup.upcustoms.pojo.CustomInfo;
import com.jinaup.upcustoms.util.HttpUtil;
import com.jinaup.upcustoms.util.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/9
 */
@Service
public class RealTimeDataUpload {
    @Autowired
    private WebSocketConfig webSocketConfig;

    public String uploadData(String customInfo){
             CustomInfo customInfo1=   JSON.toJavaObject(JSON.parseObject(customInfo), CustomInfo.class);

             StringBuilder theOrderData=  new StringBuilder();
             //拼接上传数据
             theOrderData.append("{\"sessionID\":\""+customInfo1.getSerssionID()+"\",\"payExchangeInfoHead\":{\"guid\":\""+customInfo1.getGuid()+"\",\"initalRequest\":\"aaa\",\"initalResponse\":\"ok\",\"ebpCode\":\"461216012C\",\"payCode\":\""+customInfo1.getPayCode()+"\",\"payTransactionId\":\"2018121222001354081010726129\",\"totalAmount\":"+customInfo1.getTotalAmount()+",\"currency\":\"142\",\"verDept\":\"3\",\"payType\":\"4\",\"tradingTime\":\""+customInfo1.getTradingTime()+"\",\"note\":\"aaa\"},\"payExchangeInfoLists\":[{\"orderNo\":\""+customInfo1.getOrderNo()+"\",\"goodsInfo\":"+customInfo1.getGoodsInfo()+",\"recpAccount\":\"2201080509100187672\",\"recpCode\":\"\",\"recpName\":\"海南宝贝多多网络科技有限公司\"}],\"serviceTime\":\""+customInfo1.getServiceTime()+"\",\"certNo\":\""+customInfo1.getCertNo()+"\",\"signValue\":\""+customInfo1.getSignValue()+"\"}");
             HashMap<String,String> orderDataMap= Maps.newHashMap();
             orderDataMap.put("payExInfoStr",theOrderData.toString());
             System.out.println(orderDataMap);
        /**
         * {
         *     "code":"10000",
         *     "message":"",
         *     "serviceTime":1533271903898
         * }
         */
        String s = HttpUtil.reqPost(webSocketConfig.getUploadDataUrlTs(), null, orderDataMap);
        String code="";
        if (JsonUtils.isJsonString(s)){
            JSONObject jsonObject = JSON.parseObject(s);
             code= jsonObject.getString("code");
        }
        return code;
    }
}
