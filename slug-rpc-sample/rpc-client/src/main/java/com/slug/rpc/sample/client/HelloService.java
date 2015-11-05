package com.slug.rpc.sample.client;

/**
 * @author zhw
 * @version 0.1  15/11/5
 */

public interface HelloService {
    String hello(String name);

    String hello(Person person);
}
