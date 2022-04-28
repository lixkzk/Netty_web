package com.inetty.web.common;

import com.inetty.web.log.NelLog;
import io.netty.util.AttributeKey;

public class WSConstants{
    public static final AttributeKey<String> MSGID = AttributeKey.valueOf("MSGID");
    public static final AttributeKey<Long> BEGINTIME = AttributeKey.valueOf("BEGINTIME");
    public static final AttributeKey<NelLog> LOG = AttributeKey.valueOf("LOG");

    public static final String HTTP_POST = "POST";
    public static final String HTTP_GET = "GET";
    public static final String HTTP_ALL = "ALL";
}
