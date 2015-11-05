package com.slug.rpc.sample.server;

import com.slug.rpc.sample.client.HelloService;
import com.slug.rpc.sample.client.Person;
import com.slug.rpc.server.RpcService;

/**
 * @author zhw
 * @version 0.1  15/11/5
 */
@RpcService(HelloService.class)
public class HelloServiceImpl implements HelloService {


    public String hello(String name) {
        return "Hello " + name;
    }

    public String hello(Person person) {
        return "Hello " + person.getFirstName() + " " + person.getLastName();
    }
}
