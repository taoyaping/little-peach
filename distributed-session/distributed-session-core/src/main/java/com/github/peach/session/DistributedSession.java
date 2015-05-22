package com.github.peach.session;

import javax.servlet.http.HttpSession;

/**
 * @title DistributedSession
 * @desc 分布式session管理类 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public interface DistributedSession extends HttpSession{
    
    void swapOut();
    SessionManager getManager();
    void setManager(SessionManager manager);
    HttpSession getLocalSession();
    void bindLocalSession(HttpSession session);
    boolean isInvalid();
}
