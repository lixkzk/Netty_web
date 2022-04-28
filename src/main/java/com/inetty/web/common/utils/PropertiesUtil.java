package com.inetty.web.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class PropertiesUtil {
    private static Properties env = new Properties();
    static {
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties");
            env.load(is);
        } catch (Exception e) {
            log.error("PropertiesUtil error",e);
        } finally{
            if(is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    log.error("PropertiesUtil error",e);
                }
            }
        }
    }

    public static String getProperty(String key){
        return env.getProperty(key);
    }
}
