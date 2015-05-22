/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.storage;

import com.github.peach.session.DistributedSession;

/**
 * @title MemcachedBackendSessionStorage
 * @desc session存储到memcached 
 * @author taoyaping
 * @date 2015年5月21日
 * @version 1.0
 */
public class MemcachedBackendSessionStorage implements SessionStorage{


    @Override
    public void save(DistributedSession session) {
        
    }


    @Override
    public DistributedSession load(String sessionId) {
        
        return null;
    }

    
    @Override
    public void delete(String sessionId) {
        
    }

}
