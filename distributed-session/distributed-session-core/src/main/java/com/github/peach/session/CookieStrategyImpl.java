package com.github.peach.session;

/**
 * @title CookieStrategyImpl
 * @desc 默认实现，浏览器关闭cookie过期 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public class CookieStrategyImpl implements CookieStrategy {


    @Override
    public int getExpiry() {
        return -1;
    }


    @Override
    public String getPath() {
        return "/";
    }

    
}
