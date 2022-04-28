package com.inetty.web.annotation;

import com.inetty.web.handler.NelBaseController;
import com.inetty.web.url.UrlMatch;
import com.inetty.web.url.UrlMatcher;
import lombok.Data;

@Data
public class ControllerRegistry {
    private String method;
    private String value;
    private String className;
    private Class<? extends NelBaseController> clazz;
    private UrlMatcher pattern;

    /**
     * 获取url中的参数
     *
     * @param url
     */
    public void initParse(String url,NelBaseController processor) {
        if (pattern != null && processor != null) {
            UrlMatch match = pattern.match(url);
            processor.setMatch(match);
        }
    }

    public boolean isMatch(String url) {
        boolean result = false;
        if (pattern != null) {
            result = pattern.matches(url);
        }
        return result;
    }
}
