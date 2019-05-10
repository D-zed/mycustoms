package com.jinaup.upcustoms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author 邓子迪
 * @Description TODO
 * @time 2019/5/6
 */

//@ConfigurationProperties(prefix = "WebSocket")
@Configuration
public class WebSocketConfig {


    private String url="ws://127.0.0.1:61232";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Bean
    public ServerEndpointExporter getServerEndointExpointExporter(){
        return new ServerEndpointExporter();
    }

}