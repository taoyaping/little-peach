package com.github.peach.util;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.peach.session.DistributedSession;
import com.github.peach.session.SessionManager;

/**
 * @title HttpServletRequestWrapperFilter
 * @desc 拦截http请求，包装session
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public class HttpServletRequestWrapperFilter implements Filter {
    
    private static final Logger logger = LoggerFactory.getLogger(HttpServletRequestWrapperFilter.class);
    protected SessionManager sessionManager;
    
    @Override
    public final void init(FilterConfig filterConfig) throws ServletException {
        String sessionManagerClass = filterConfig.getInitParameter("sessionManager");
        String hostName = filterConfig.getInitParameter("hostName");
        try {
            Object sessionManagerObj = Class.forName(sessionManagerClass).newInstance();
            sessionManager = (SessionManager)sessionManagerObj;
            sessionManager.setHostName(hostName);
        } catch (Throwable e) {
            throw new RuntimeException("Load session manager:"+sessionManagerClass+" failed ...", e);
        }
        
        initInternal(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
        ServletException {
        
        final DistributedHttpServletRequestWrapper requestWrapper =
            new DistributedHttpServletRequestWrapper((HttpServletRequest) request, (HttpServletResponse) response);
        try {
            preFilter(requestWrapper, response);
            chain.doFilter(requestWrapper, response);
            afterFilter(requestWrapper, response);
        } finally {
            HttpSession session = requestWrapper.getSession(false);
            if(null != session) {
                if(session instanceof DistributedSession) {
                    if(logger.isDebugEnabled()) {
                        logger.debug("Try sync session：{}", session);
                    }
                    
                    sessionManager.swapOut((DistributedSession)session);           
                }
            }
        }
        
    }

    @Override
    public void destroy() {

    }
    
    protected  void initInternal(FilterConfig filterConfig) {
        //no op
    }
    
    protected void preFilter(ServletRequest request, ServletResponse response) {
        //no op
    }
    
    protected void afterFilter(ServletRequest request, ServletResponse response) {
       //no op
    }
 

    final class DistributedHttpServletRequestWrapper extends HttpServletRequestWrapper {

        final HttpServletRequest request;
        final HttpServletResponse response;
        
        public DistributedHttpServletRequestWrapper(HttpServletRequest request, HttpServletResponse response) {
            super(request);
            this.request = request;
            this.response = response;
        }

        @Override
        public HttpSession getSession(boolean create) {
            return sessionManager.getSession(request, response, create);
        }

        @Override
        public HttpSession getSession() {
            return getSession(true);
        }  
    }
}
