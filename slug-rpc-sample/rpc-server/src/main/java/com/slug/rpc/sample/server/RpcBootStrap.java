package com.slug.rpc.sample.server;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author zhw
 * @version 0.1  15/11/5
 */
public class RpcBootStrap {

    public static void main(String[] args) {
        new ClassPathXmlApplicationContext("spring.xml");
    }

}
