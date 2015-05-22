/**
 * Baijiahulian.com Inc.
 * Copyright (c) 2014-2015 All Rights Reserved.
 */
package com.github.peach.util;

import java.util.UUID;

/**
 * @title SessionIdUtil
 * @desc TODO 
 * @author taoyaping
 * @date 2015年5月21日
 * @version 1.0
 */
public class SessionIdUtil {

    public static String generateSessionId() {
        String sessionId = UUID.randomUUID().toString();
        sessionId = sessionId.replaceAll("-", "").toLowerCase();
        return sessionId;
    }
}
