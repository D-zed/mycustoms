package com.jinaup.upcustoms.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/6
 */

@Configuration
@Data
public class WebSocketConfig {

    @Value("${customs.CustomInfoStatusUrl}")
    private   String  jinaupStatusUrl;
    @Value("${customs.wsUrl}")
    private  String  wsUrl;
    @Value("${customs.uploadDataUrlTs}")
    private  String uploadDataUrlTs;
    @Value("${customs.uploadDataUrl}")
    private  String uploadDataUrl;

    @Bean
    public ServerEndpointExporter getServerEndointExpointExporter(){
        return new ServerEndpointExporter();
    }

}