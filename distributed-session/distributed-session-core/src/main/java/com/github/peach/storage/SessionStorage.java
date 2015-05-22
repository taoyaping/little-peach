package com.github.peach.storage;

import com.github.peach.session.DistributedSession;

/**
 * @title SessionStorage
 * @desc session 存储 
 * @author taoyaping
 * @date 2015年5月21日
 * @version 1.0
 */
public interface SessionStorage {

    void save(DistributedSession session);
    DistributedSession load(String sessionId);
    void delete(String sessionId);
}
