/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.session;

/**
 * @title CookieStrategy
 * @desc cookie过期策略类 
 * @author taoyaping
 * @date 2015年5月20日
 * @version 1.0
 */
public interface CookieStrategy {

    String getPath();
    int getExpiry();
}
