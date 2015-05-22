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
    
    @Override
    public DistributedSession getSession(HttpServletRequest request, HttpServletResponse response, boolean create) {
        String sessionId = getSessionCookieManager().searchSessionCookie(request);
        if(StringUtils.isNotBlank(sessionId)) {
            logger.info("Searched session cookie:{}", sessionId);
        }else {
            sessionId = getSessionCookieManager().createSessionCookie(response);
            logger.info("Created session cookie:{}", sessionId);
        }
        
        return doGetSession(request, sessionId, create);
    }
    
    @Override
    public void swapOut(DistributedSession session) {
        
        Object sessionLock = session.getLocalSession();
        
        synchronized (sessionLock) {
            if(session.isInvalid()) {
                if(logger.isDebugEnabled()) {
                    logger.debug("Try delete session:{}", JacksonUtil.safeObj2Str(session));
                }
                getSessionStorage().delete(session.getId());
                logger.info("Session:{} deleted.", session.getId());
            }else {
                
                if(logger.isDebugEnabled()) {
                    logger.debug("Try save session:{}", JacksonUtil.safeObj2Str(session));
                }
                getSessionStorage().save(session);
                logger.info("Session:{} saved.", session.getId());
            }
        }
    }

    protected SessionCookieManager getSessionCookieManager() {
        return new SessionCookieManagerImpl();
    }

    protected DistributedSession doGetSession(HttpServletRequest request, String sessionId, boolean create) {
        boolean newSession = false;
        DistributedSession session = getSessionStorage().load(sessionId);
 
        if(null == session) {            
            if(create) {
                Object sessionLock = request.getSession();
                synchronized (sessionLock) {
                    session = new AbstractDistributedSession(sessionId);
                    newSession = true;
                    getSessionStorage().save(session);
                }
                
                logger.info("Get session:{} by create one.", session.getId());
            }

        }else {
            logger.info("Get session:{} from session storage.", session.getId());
        }
        
        if(null != session) {
            session.setManager(this);
            if(!newSession) { 
                copyDistributeSessionAttrisToHttpSession(session, request.getSession());
            }
            session.bindLocalSession(request.getSession());
            
            if(logger.isDebugEnabled()) {
                logger.debug("Get session:{} from session storage.", JacksonUtil.safeObj2Str(session));    
            }
        }
        
        return session;
    }
    
    
    protected void copyDistributeSessionAttrisToHttpSession(DistributedSession session, HttpSession httpSession) {
        @SuppressWarnings("unchecked")
        Enumeration<String> attributes = session.getAttributeNames();
        while(attributes.hasMoreElements()) {
            String attriName = attributes.nextElement();
            httpSession.setAttribute(attriName, session.getAttribute(attriName));
        }
    }
    
    protected abstract SessionStorage getSessionStorage();
    
}
