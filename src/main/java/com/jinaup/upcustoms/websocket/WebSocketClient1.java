package com.jinaup.upcustoms.websocket;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.jinaup.pojo.customs.CustomInfo;
import com.jinaup.service.coop.custominfoserviceimpl.CustomInfoServiceImpl;
import com.jinaup.upcustoms.pojo.CustomInfo;
import com.jinaup.upcustoms.service.RealTimeDataUpload;
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
public class WebSocketClient1 {


    private static RealTimeDataUpload realTimeDataUpload;

    public static void setRealTimeDataUpload(RealTimeDataUpload realTimeDataUpload) {
        WebSocketClient1.realTimeDataUpload = realTimeDataUpload;
    }

    private static CustomInfo customInfo;
    //上传接口这个其实是不与加签部署
    private static final String uploadDataUrl="http://127.0.0.1:80/uploadTimeData";

    public static CustomInfo getCustomInfo() {
        return customInfo;
    }
    //回调状态
    private static CustomInfoServiceImpl customInfoService;

    public static void setCustomInfoService(CustomInfoServiceImpl customInfoService) {
        WebSocketClient1.customInfoService = customInfoService;
    }

    public static void setCustomInfo(CustomInfo customInfo) {
        WebSocketClient1.customInfo = customInfo;
    }

    public WebSocketClient1() {
    }

    private static Log log= LogFactory.getLog(WebSocketClient1.class);
    private static String url="ws://127.0.0.1:61232";
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
    public void onMessage(String message) {

        System.out.println("server onMessage: 收到消息" + message);
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

                //这里目前这么测试，都在一个服务器不好远程调用啊
                //RealTimeDataUpload realTimeDataUpload = new RealTimeDataUpload();

                String s = realTimeDataUpload.uploadData(s1);
                log.info(s+"这个是上传数据的方法");

                // TODO 这个地方进行判断最终上传是否成功，如果成功则回调更改的方法 将临时 海关表中的数据状态 改成 1    这个目前不可以 自动注入
                //更改状态
                //TODO  这个还需要做个判断，并且表中应该加入字段 创建时间 ，并且2分钟后如果 状态还没改成 状态那么就会对其进行失效操作  也就是状态改为 2 就代表 这个是一个处理失败的
                //CustomInfoService customInfoService=new CustomInfoServiceImpl();
                int updatecustominfostatus = customInfoService.updatecustominfostatus(customInfo.getOrderNo());
                if (updatecustominfostatus>0){
                    log.info("update status is ok");
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
    public static void inticonn(){
        if (webSocketContainer!=null){
            return;
        }
        webSocketContainer = ContainerProvider.getWebSocketContainer();
        try {
            try {
                webSocketContainer.connectToServer(WebSocketClient1.class,new URI(url));
            } catch (URISyntaxException e) {

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
