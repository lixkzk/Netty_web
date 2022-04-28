package com.inetty.web.handler;

import com.inetty.web.annotation.ControllerRegistry;
import com.inetty.web.common.WSConstants;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NelProcessorFactory {
    private static Map<String, ControllerRegistry> nelControllerRegistryList = new ConcurrentHashMap<String, ControllerRegistry>();

    public NelBaseController create(String url, HttpMethod method) {
        NelBaseController processor = null;
        try {
            for (Iterator<String> iter = nelControllerRegistryList.keySet().iterator(); iter.hasNext(); ) {
                String kry = iter.next();
                ControllerRegistry cr = nelControllerRegistryList.get(kry);
                if (cr != null) {
                    if (WSConstants.HTTP_ALL.equals(cr.getMethod())) {
                        if (cr.isMatch(url)) {
                            Class<? extends NelBaseController> cls = cr.getClazz();
                            if (cls != null) {
                                processor = cls.newInstance();
                                cr.initParse(url, processor);
                                break;
                            }
                        }
                    } else {
                        if (cr.getMethod().equals(method.name())) {
                            if (cr.isMatch(url)) {
                                Class<? extends NelBaseController> cls = cr.getClazz();
                                if (cls != null) {
                                    processor = cls.newInstance();
                                    cr.initParse(url, processor);
                                    break;
                                }
                            }
                        }
                    }
                } else {
                    log.info("create controller error! url = " + kry);
                }
            }
        } catch (Exception e) {
            log.error("create controller error!" + url, e);
        }
        return processor;
    }

    public static void registerController(ControllerRegistry registry) {
        if (registry != null) {
            nelControllerRegistryList.put(registry.getValue(), registry);
        }
    }

    public static Map<String, ControllerRegistry> queryRegistry() {
        return nelControllerRegistryList;
    }

}
