package com.inetty.web.start;

import com.inetty.web.common.utils.PropertiesUtil;
import com.inetty.web.manager.TestResourceManagerImpl;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;

public class NelApplication {
    static TestResourceManagerImpl resourceManager;

    static{
        resourceManager = new TestResourceManagerImpl();
        resourceManager.setServerName(PropertiesUtil.getProperty("nel.serverName"));
        resourceManager.setServerIp(PropertiesUtil.getProperty("nel.serverIp"));
        resourceManager.setPort(Integer.parseInt(PropertiesUtil.getProperty("nel.port")));
        resourceManager.setServerRegion(PropertiesUtil.getProperty("nel.serverRegion"));
        resourceManager.setBossGroupThreadNums(Integer.parseInt(PropertiesUtil.getProperty("nel.bossGroupThreadNums")));
        resourceManager.setExecutorThreadNums(Integer.parseInt(PropertiesUtil.getProperty("nel.executorThreadNums")));
        resourceManager.setInterval(Integer.parseInt(PropertiesUtil.getProperty("nel.interval")));
        resourceManager.setScanPackage(PropertiesUtil.getProperty("nel.scanPackage"));
        resourceManager.setRefuseConns(Integer.parseInt(PropertiesUtil.getProperty("nel.refuseConns")));

        PropertyConfigurator.configure("src/main/resources/log4j.properties");
        BasicConfigurator.configure();
    }

    public static void main(String[] args){
        NelServer nelServer = new NelServer();
        nelServer.init(resourceManager).start();
    }
}
