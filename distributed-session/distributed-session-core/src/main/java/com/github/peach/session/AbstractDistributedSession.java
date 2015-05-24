/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import com.github.peach.util.JacksonUtil;


/**
 * @title AbstractDistributedHttpSession
 * @desc TODO 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public class AbstractDistributedSession implements DistributedSession, Serializable {

    private static final long serialVersionUID = 3225146473022966126L;
    
    protected transient SessionManager sessionManager;
    
    protected transient HttpSession session;
    
    protected String cachedSessionId;
    
    protected transient volatile boolean isValid = true;
    
    protected ConcurrentHashMap<String, Object> cachedAttributes = new ConcurrentHashMap<String, Object>();
    
    public AbstractDistributedSession(String cachedSessionId) {
        this.cachedSessionId = cachedSessionId;
    }
    
    @Override
    public void bindLocalSession(HttpSession session) {
        this.session = session;
        this.session.setAttribute(session.getId(), this);
    }

    @Override
    public SessionManager getManager() {
        return this.sessionManager;
    }


    @Override
    public void setManager(SessionManager manager) {
        this.sessionManager = manager;
    }


    @Override
    public void swapOut() {
        if(null != sessionManager) {
            sessionManager.swapOut(this);
        }
        
    }
    
    @Override
    public void swapIn() {
        if(null != sessionManager) {
            sessionManager.swapIn(this);
        }
    }
    
    
    
    @Override
    public String getCachedSessionId() {
        return cachedSessionId;
    }

    @Override
    public long getCreationTime() {
        return session.getCreationTime();
    }


    @Override
    public String getId() {
        return session.getId();   
    }


    @Override
    public long getLastAccessedTime() {
        return session.getLastAccessedTime();
    }


    @Override
    public ServletContext getServletContext() {
        return session.getServletContext();
    }


    @Override
    public void setMaxInactiveInterval(int interval) {
        session.setMaxInactiveInterval(interval);
    }

    @Override
    public int getMaxInactiveInterval() {
        return session.getMaxInactiveInterval();
    }


    @Override
    public HttpSessionContext getSessionContext() {
        return session.getSessionContext();
    }


    @Override
    public Object getAttribute(String name) {
        
        return cachedAttributes.get(name);
    }


    @Override
    public Object getValue(String name) {
        return getAttribute(name);
    }


    @Override
    public Enumeration getAttributeNames() {
        
        return cachedAttributes.keys();
    }

    

    @Override
    public String[] getValueNames() {
     
        List<String> namesList = new ArrayList<String>(cachedAttributes.keySet());        
        return namesList.toArray(new String[namesList.size()]);
    }


    @Override
    public void setAttribute(String name, Object value) {
        cachedAttributes.put(name, value);
        swapOut();
    }


    @Override
    public void putValue(String name, Object value) {
        setAttribute(name, value);
    }


    @Override
    public void removeAttribute(String name) {
        cachedAttributes.remove(name);
        swapOut();
    }


    @Override
    public void removeValue(String name) {
        removeAttribute(name);
    }

    @Override
    public void invalidate() {
        
        if(isValid) {
            synchronized (this) {
                if(isValid) {
                    isValid = false;
                    cachedAttributes.clear();
                    session.invalidate();
                    
                }
            }
        }
        
        if(isValid) {
            swapOut();
        }
    }


    @Override
    public boolean isNew() {
        return session.isNew();
    }


    @Override
    public boolean isInvalid() {
        return !isValid;
    }


    @Override
    public HttpSession getLocalSession() {
        return session;
    }

    @Override
    public String toString() {
        return "DistributedSession [cachedSessionId=" + cachedSessionId + ", isValid=" + isValid
            + ", cachedAttributes=" + JacksonUtil.safeObj2Str(cachedAttributes) + "]";
    }
}
