package com.inetty.web.manager;

public interface NelResourceManager{
    public String getServerName();
    public Integer getPort();
    public Integer getBossGroupThreadNums();
    public Integer getExecutorThreadNums();
    public Integer getRefuseConns();
    public String getScanPackage();
    public Integer getInterval();
    public String getServerIp();
    public String getServerRegion();

}
