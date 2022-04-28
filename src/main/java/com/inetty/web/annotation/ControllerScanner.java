package com.inetty.web.annotation;

import com.inetty.web.common.utils.PackageUtil;
import com.inetty.web.handler.NelBaseController;
import com.inetty.web.handler.NelProcessorFactory;
import com.inetty.web.url.UrlPattern;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ControllerScanner {
    private List<ControllerRegistry> handlers = new ArrayList<ControllerRegistry>();
    private Map<String, ControllerRegistry> filters = new HashMap<String, ControllerRegistry>();
    private Class<? extends NelBaseController> defaultClazz;

    public void scan(String packageName) {
        scan(Thread.currentThread().getContextClassLoader(), packageName);
    }

    public void scan(ClassLoader classLoader, String packageName) {
        List<String> list = PackageUtil.getClassName(classLoader, packageName);
        for (String className : list) {
            try {
                Class<?> clazz = classLoader.loadClass(className);
                if (NelBaseController.class.isAssignableFrom(clazz)) {
                    if (clazz.isAnnotationPresent(Controller.class)) {
                        Controller anno = clazz.getAnnotation(Controller.class);
                        ControllerRegistry registry = new ControllerRegistry();
                        registry.setClazz((Class<? extends NelBaseController>) clazz);
                        registry.setMethod(anno.method());
                        registry.setValue(anno.value());
                        registry.setPattern(new UrlPattern(anno.value()));
                        /**
                         if（clazz.isAnnotationPresent(Filter.class)）{
                         Filter annoFilter = clazz.getAnnotation(Filter.class);
                         filters.put(annoFilter.value(),registry);
                         }else{
                         handlers.add(registry);
                         }**/
                        NelProcessorFactory.registerController(registry);
                    }
                }
            } catch (ClassNotFoundException e) {
                log.error("");
            }
        }
        if (defaultClazz != null) {
            ControllerRegistry registry = new ControllerRegistry();
            registry.setClazz(defaultClazz);
            handlers.add(registry);
        }
    }

    public List<ControllerRegistry> getHandlers() {
        return handlers;
    }

    public Map<String, ControllerRegistry> getFilters() {
        return filters;
    }

    public ControllerRegistry getFilter(String name) {
        return filters.get(name);
    }
}
