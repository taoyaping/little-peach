/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.util;

import net.rubyeye.xmemcached.MemcachedClient;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @title MemcachedClientLocator
 * @desc TODO 
 * @author taoyaping
 * @date 2015年5月22日
 * @version 1.0
 */
public class MemcachedClientLocator implements ApplicationContextAware {

    private static MemcachedClient memcachedClient;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        memcachedClient = applicationContext.getBean("memcachedClient", MemcachedClient.class);
        if(null == memcachedClient) {
            throw new RuntimeException("Memcached client did not configure right in spring context ....");
        }
    }

    public static MemcachedClient getMemcachedClient() {
        return memcachedClient;
    }
}
