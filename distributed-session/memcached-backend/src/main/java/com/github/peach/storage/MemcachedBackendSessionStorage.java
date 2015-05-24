/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.storage;

import java.util.concurrent.TimeoutException;

import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.exception.MemcachedException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.peach.session.DistributedSession;
import com.github.peach.util.MemcachedClientLocator;

/**
 * @title MemcachedBackendSessionStorage
 * @desc session存储到memcached 
 * @author taoyaping
 * @date 2015年5月21日
 * @version 1.0
 */
public class MemcachedBackendSessionStorage implements SessionStorage{

    private static final Logger logger = LoggerFactory.getLogger(MemcachedBackendSessionStorage.class);
    
    /**
     * memcached中比local session额外多设置的超时时间
     */
    private static final int ADDITIONAL_EXPIREY = 5 * 60;
    private static final int RETRY = 3;
    /**
     * 操作timeout，单位ms
     */
    private static final int OPT_TIMEOUT = 1000;

    @Override
    public void save(DistributedSession session) {
        
        if(null == session) {
            return;
        }
        
        int retry = 0;
        while(retry++ < RETRY) {
            int expiry = session.getMaxInactiveInterval() + ADDITIONAL_EXPIREY;
            try {
                getMemcachedCLient().set(session.getCachedSessionId(), expiry, session, OPT_TIMEOUT);
                  
                if(logger.isDebugEnabled()) {
                    logger.debug("Save session:{} to memcached.", session);
                }else {
                    logger.info("Save session:{} to memcached.", session.getCachedSessionId());
                }
                
                return;
             
            } catch (TimeoutException | InterruptedException | MemcachedException e) {
                if(retry == RETRY) {
                    logger.error("Save session:" + session.getCachedSessionId() +" from memcached error, after retry "+RETRY+" times ...", e);
                }else {
                    logger.warn("Save session:" + session.getCachedSessionId() +" from memcached error, retry "+retry+" times ...", e);
                }
            }
        }
    }


    @Override
    public DistributedSession load(String sessionId) {
        
        if(StringUtils.isBlank(sessionId)) {
            return null;
        }
        
        int retry = 0;
        while(retry++ < RETRY) {
            try {
                DistributedSession session = getMemcachedCLient().get(sessionId, OPT_TIMEOUT);
                         
                if(logger.isDebugEnabled()) {
                    logger.debug("Load session:{} from memcached.", session);
                }else {
                    logger.info("Load session:{} from memcached.", sessionId);
                }
                
                return session;
            } catch (TimeoutException | InterruptedException | MemcachedException e) {
                if(retry == RETRY) {
                    logger.error("Load session:" + sessionId +" from memcached error, after retry "+RETRY+" times ...", e);
                }else {
                    logger.warn("Load session:" + sessionId +" from memcached error, retry "+retry+" times ...", e);
                }
                
            }
        }
        
        return null;
    }

    
    @Override
    public void delete(String sessionId) {
        
        if(StringUtils.isBlank(sessionId)) {
            return;
        }
        int retry = 0;
        while (retry++ < RETRY) {
            try {
                getMemcachedCLient().delete(sessionId);
                return;
            } catch (TimeoutException | InterruptedException | MemcachedException e) {
                if(retry == RETRY) {
                    logger.error("Delete session:" + sessionId +" from memcached error, after retry "+RETRY+" times ...", e);
                }else {
                    logger.warn("Delete session:" + sessionId +" from memcached error, retry "+retry+" times ...", e);
                }
            }
        }
    }

    protected MemcachedClient getMemcachedCLient() {
        return MemcachedClientLocator.getMemcachedClient();
    }
}
