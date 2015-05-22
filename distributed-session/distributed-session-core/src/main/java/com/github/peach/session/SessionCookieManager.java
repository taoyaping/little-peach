package com.github.peach.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @title SessionCookieManager
 * @desc session cookie管理类 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public interface SessionCookieManager {
    
    /**
     * 返回cookie名称
     * @return
     */
    String getSessionCookieName();
    
    /**
     * 返回cookie的后缀，使cookie更可读，也不容易冲突 
     * @return
     */
    String getSessionCookieSuffix();
    
    /**
     * 从request里搜索保存sessionId的cookie
     * @param request
     * @return 搜索到的sessionId, null 如果搜索失败
     */
    String searchSessionCookie(HttpServletRequest request);
    
    /**
     * 创新sessionId，并保存到cookie
     * @param response
     * @return
     */
    String createSessionCookie(HttpServletResponse response);
    
    /**
     * 返回创建cookie的策略对象
     * @return
     */
    CookieStrategy getCookieStrategy();
}
