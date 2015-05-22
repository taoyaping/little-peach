package com.github.peach.session;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.peach.storage.SessionStorage;
import com.github.peach.util.JacksonUtil;

/**
 * @title AbstractSessionManager
 * @desc session管理器
 * @author taoyaping
 * @date 2015年5月21日
 * @version 1.0
 */
public abstract class AbstractSessionManager implements SessionManager {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractSessionManager.class);
    protected Map<String,Object> sessionLockMap = new HashMap<String,Object>();
    protected String hostName = "localhost";
    
    @Override
    public DistributedSession getSession(HttpServletRequest request, HttpServletResponse response, boolean create) {
        
        HttpSession session = request.getSession(create);
        if(null == session) {
            return null;
        } 
        
        String cachedSessionId = session.getId() +"."+ getHostName();
        
        return doGetSession(session, cachedSessionId);
    }
    
    @Override
    public void swapOut(DistributedSession session) {
        
        final Object sessionLock = session.getLocalSession();
        
        synchronized (sessionLock) {
            if(session.isInvalid()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Try delete session:{}", JacksonUtil.safeObj2Str(session));
                }
                getSessionStorage().delete(session.getCachedSessionId());
                logger.info("Session:{} deleted.", session.getCachedSessionId());
            }else {
                
                if(logger.isDebugEnabled()) {
                    logger.debug("Try save session:{}", JacksonUtil.safeObj2Str(session));
                }
                getSessionStorage().save(session);
                logger.info("Session:{} saved.", session.getCachedSessionId());
            }
        }
    }
    
    @Override
    public void swapIn(DistributedSession session) {
        DistributedSession cachedSession = getSessionStorage().load(session.getCachedSessionId());
        if(null != cachedSession) {
            copySessionAttributes(cachedSession, session);
        }
    }
    

    @Override
    public String getHostName() {
        return hostName;
    }

    @Override
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }


    protected SessionCookieManager getSessionCookieManager() {
        return new SessionCookieManagerImpl();
    }

    protected DistributedSession doGetSession(HttpSession httpSession, String cachedSessionId) {
        
        DistributedSession session = getBindedDistributeSession(httpSession);
        
        if(null != session) {             
            session.swapIn();
        }else { //new session
            
            synchronized (httpSession) {
                session = new AbstractDistributedSession(cachedSessionId);
                session.setManager(this);               
                session.bindLocalSession(httpSession);
            }
            
            session.swapIn();                  
        }
        
        return session;
    }
    
    protected void copySessionAttributes(HttpSession src, HttpSession tgt) {
        @SuppressWarnings("unchecked")
        Enumeration<String> attributes = src.getAttributeNames();
        while(attributes.hasMoreElements()) {
            String attriName = attributes.nextElement();
            tgt.setAttribute(attriName, src.getAttribute(attriName));
        }
    }
    
    protected DistributedSession getBindedDistributeSession(HttpSession httpSession) {
        return (DistributedSession)httpSession.getAttribute(httpSession.getId()); 
    }
    
    
    
    protected abstract SessionStorage getSessionStorage();
    
}
