/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.session;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.github.peach.util.SessionIdUtil;

/**
 * @title SessionCookieManagerImpl
 * @desc 默认实现类 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public class SessionCookieManagerImpl implements SessionCookieManager {

    private static final String SESSION_COOKIE_NAME = "_const_d_jsession_id_";

    @Override
    public String getSessionCookieName() {
        return SESSION_COOKIE_NAME;
    }

    
    @Override
    public String searchSessionCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String sessionId = null;
        if(null != cookies) {
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(getSessionCookieName())) {
                    sessionId = cookie.getValue();
                }
            }
        }
        return StringUtils.isNotBlank(sessionId) ? sessionId : null;
    }


    @Override
    public String createSessionCookie(HttpServletResponse response) {
        String cookieValue = SessionIdUtil.generateSessionId() + getSessionCookieSuffix();
        Cookie cookie = new Cookie(getSessionCookieName(), cookieValue);
        cookie.setPath(getCookieStrategy().getPath());
        cookie.setMaxAge(getCookieStrategy().getExpiry());
        response.addCookie(cookie);
        return cookieValue;
    }



    @Override
    public CookieStrategy getCookieStrategy() {
        return new CookieStrategyImpl();
    }



    @Override
    public String getSessionCookieSuffix() {
        return "-";
    }
}
