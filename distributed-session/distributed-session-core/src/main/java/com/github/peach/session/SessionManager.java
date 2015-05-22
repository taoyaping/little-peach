package com.github.peach.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title SessionManager
 * @desc session 管理器 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public interface SessionManager {
    
    DistributedSession getSession(HttpServletRequest request, HttpServletResponse response, boolean create);
    void swapIn(DistributedSession session);
    void swapOut(DistributedSession session);
    String getHostName();
    void setHostName(String hostName);
}
