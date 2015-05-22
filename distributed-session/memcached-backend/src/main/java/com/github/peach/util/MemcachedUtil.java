package com.github.peach.util;

import java.io.IOException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;

/**
 * @title MemcachedUtil
 * @desc TODO 
 * @author taoyaping
 * @date 2015年5月22日
 * @version 1.0
 */
public class MemcachedUtil {

    private static MemcachedClient memcachedClient; 
    
    static {
        MemcachedClientBuilder memcachedClientBuilder = new XMemcachedClientBuilder();
        try {
            memcachedClient = memcachedClientBuilder.build();
        } catch (IOException e) {
            throw new RuntimeException("Init memcached client failed ...", e);
        }
    }
}
