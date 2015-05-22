/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.session;

import java.io.Serializable;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;


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
    
    protected HttpSession session;
    
    protected String id;
    
    protected volatile boolean isValid = true;
    
    public AbstractDistributedSession(String sessionId) {
        this.id = sessionId;
    }
    
    @Override
    public void bindLocalSession(HttpSession session) {
        this.session = session;
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
    public long getCreationTime() {
        return session.getCreationTime();
    }


    @Override
    public String getId() {
        return this.id;    
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
        return session.getAttribute(name);
    }


    @Override
    public Object getValue(String name) {
        return session.getValue(name);
    }


    @Override
    public Enumeration getAttributeNames() {
        return session.getAttributeNames();
    }


    @Override
    public String[] getValueNames() {
        return session.getValueNames();
    }


    @Override
    public void setAttribute(String name, Object value) {
        session.setAttribute(name, value);
        swapOut();
    }


    @Override
    public void putValue(String name, Object value) {
        session.putValue(name, value);
        swapOut();
    }


    @Override
    public void removeAttribute(String name) {
        session.removeAttribute(name);
        swapOut();
    }


    @Override
    public void removeValue(String name) {
        session.removeValue(name);
        swapOut();
    }

    @Override
    public void invalidate() {
        if(!isValid) {
            return;
        }
        
        synchronized (this) {
            session.invalidate();
            isValid = false;            
        }
        
        swapOut();
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
}
