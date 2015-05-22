/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.session;

import com.github.peach.storage.MemcachedBackendSessionStorage;
import com.github.peach.storage.SessionStorage;

/**
 * @title MemcachedBackendSessionManager
 * @desc 基于memcached的session管理 
 * @author taoyaping
 * @date 2015年5月22日
 * @version 1.0
 */
public class MemcachedBackendSessionManager extends AbstractSessionManager {


    @Override
    protected SessionStorage getSessionStorage() {
        return new MemcachedBackendSessionStorage();
    }

}
