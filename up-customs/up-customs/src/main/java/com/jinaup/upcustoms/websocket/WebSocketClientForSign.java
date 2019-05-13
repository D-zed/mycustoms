package com.jinaup.upcustoms.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jinaup.upcustoms.config.WebSocketConfig;
import com.jinaup.upcustoms.pojo.CustomInfo;
import com.jinaup.upcustoms.service.RealTimeDataUpload;
import com.jinaup.upcustoms.util.HttpUtil;
import com.jinaup.upcustoms.util.JsonUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/8
 */
@Component
@ClientEndpoint
public class WebSocketClientForSign {
    private static WebSocketConfig webSocketConfig;

    private static   RealTimeDataUpload realTimeDataUpload;

    private static CustomInfo customInfo;

    public static CustomInfo getCustomInfo() {
        return customInfo;
    }

    public static WebSocketConfig getWebSocketConfig() {
        return webSocketConfig;
    }

    public static void setWebSocketConfig(WebSocketConfig webSocketConfig) {
        WebSocketClientForSign.webSocketConfig = webSocketConfig;
    }

    public static RealTimeDataUpload getRealTimeDataUpload() {
        return realTimeDataUpload;
    }

    public static void setRealTimeDataUpload(RealTimeDataUpload realTimeDataUpload) {
        WebSocketClientForSign.realTimeDataUpload = realTimeDataUpload;
    }

    public static void setCustomInfo(CustomInfo customInfo) {
        WebSocketClientForSign.customInfo = customInfo;
    }

    public WebSocketClientForSign() {
    }

    private static Log log= LogFactory.getLog(WebSocketClientForSign.class);

    //session缓存
    public static  HashMap<String,Session> sessionMap= Maps.newHashMap();
    @OnOpen
    public void onOpen(Session session) {
        log.info("==建立连接==sessionid"+session.getId());
        //将session存入map
        sessionMap.put("1", session);
    }

    //加签的返回值在这里 ， 并且在这里获取到数据就立即进行数据的上传
    @OnMessage
    public void onMessage(String message) throws Exception {

        System.out.println("server onMessage: 收到消息" + message);

        if (!JsonUtils.isJsonString(message)){
            throw new Exception("这个并非jsonstring");
        }
        JSONObject jsonObject = JSON.parseObject(message);
        Integer id = jsonObject.getInteger("_id");
        //id是1对其解析
        if (id==1){
            JSONObject args = jsonObject.getJSONObject("_args");
            //如果是true则需要解析
            if (args.getBoolean("Result")){
                JSONArray data = args.getJSONArray("Data");
                //解析出来signvalue 和 证书编号
                List<String> resultset = data.toJavaList(String.class);
                log.info("返回值"+resultset);
                customInfo.setSignValue(resultset.get(0));
                customInfo.setCertNo(resultset.get(1));

                String s1 = JSON.toJSONString(customInfo);

                //调用数据上传的方法 进行接口的调用
                String s = realTimeDataUpload.uploadData(s1);
                log.info(s+"返回状态码");
                //回调修改状态
                if("10000".equals(s))
                {
                    HashMap<String,String> jinaupMap=Maps.newHashMap();
                    jinaupMap.put("orderNo",customInfo.getOrderNo());
                    //发出请求并且进行结果的获取
                    String s2 = HttpUtil.reqPost(webSocketConfig.getJinaupStatusUrl(), null, jinaupMap);

                    if (s2!=null&& "ok".equals(s2)){
                        log.info("update status is ok");
                    }else{
                            throw new Exception("修改状态失败");
                    }
                }


                /*Map<String,String> nvps=Maps.newHashMap();
                nvps.put("cutomInfo",JSON.toJSONString(customInfo));
                String s = HttpUtil.reqPost(uploadDataUrl, null, nvps);*/
                /*HttpClient httpClient= HttpClients.createDefault();
                HttpPost post=null;
                ArrayList<NameValuePair> nvps = Lists.newArrayList();
                nvps.add(new BasicNameValuePair("cutomInfo",JSON.toJSONString(customInfo)));
                try {
                    post=new HttpPost(uploadDataUrl);
                    post.setEntity(new UrlEncodedFormEntity(nvps,"UTF-8"));
                    HttpResponse execute = httpClient.execute(post);
                    String result = EntityUtils.toString(execute.getEntity());
                    if (execute.getStatusLine().getStatusCode()!= HttpStatus.SC_OK){
                        log.error("sign is error");
                    }else{

                        log.info("sign is ok"+result);
                    }

                }catch (Exception e){
                        e.printStackTrace();
                }finally {
                    try {
                        ((CloseableHttpClient) httpClient).close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }*/



            }
        }
    }
    @OnClose
    public void onClose() {
        log.info("============关闭======================");
    }
    @OnError
    public void onError(Throwable t) {
        t.printStackTrace();
    }


    public static WebSocketContainer webSocketContainer=null;
    public  void inticonn(){
        if (webSocketContainer!=null){
            return;
        }
        webSocketContainer = ContainerProvider.getWebSocketContainer();
        String wsUrl = webSocketConfig.getWsUrl();
        try {
            try {
                webSocketContainer.connectToServer(WebSocketClientForSign.class,new URI(wsUrl));
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        } catch (DeploymentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //向服务端发送消息的方法
    public static  void sendMsgToServer(Session session,String message){
        try {
            //发送数据
            log.info("发送数据"+message);
            session.getBasicRemote().sendText(message);
        }catch (Exception e){
            //发送异常的话就缓存在redis中
        }

    }

}
