package com.jinaup.upcustoms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class UpCustomsApplication {

    public static void main(String[] args) {
        SpringApplication.run(UpCustomsApplication.class, args);
    }

}
