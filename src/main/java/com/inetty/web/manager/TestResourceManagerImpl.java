package com.inetty.web.manager;

public class TestResourceManagerImpl implements NelResourceManager {

    String serverName;
    Integer port;
    Integer bossGroupThreadNums;
    Integer executorThreadNums;
    Integer refuseConns;
    String scanPackage;
    Integer interval;
    String serverIp;
    String serverRegion;

    @Override
    public String getServerName() {
        return this.serverName;
    }

    @Override
    public Integer getPort() {
        return this.port;
    }

    @Override
    public Integer getBossGroupThreadNums() {
        return this.bossGroupThreadNums;
    }

    @Override
    public Integer getExecutorThreadNums() {
        return this.executorThreadNums;
    }

    @Override
    public Integer getRefuseConns() {
        return this.refuseConns;
    }

    @Override
    public String getScanPackage() {
        return this.scanPackage;
    }

    @Override
    public Integer getInterval() {
        return this.interval;
    }

    @Override
    public String getServerIp() {
        return this.serverIp;
    }

    @Override
    public String getServerRegion() {
        return this.serverRegion;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public void setBossGroupThreadNums(Integer bossGroupThreadNums) {
        this.bossGroupThreadNums = bossGroupThreadNums;
    }

    public void setExecutorThreadNums(Integer executorThreadNums) {
        this.executorThreadNums = executorThreadNums;
    }

    public void setRefuseConns(Integer refuseConns) {
        this.refuseConns = refuseConns;
    }

    public void setScanPackage(String scanPackage) {
        this.scanPackage = scanPackage;
    }

    public void setInterval(Integer interval) {
        this.interval = interval;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public void setServerRegion(String serverRegion) {
        this.serverRegion = serverRegion;
    }
}
