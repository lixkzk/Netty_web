package com.inetty.web.log;

import lombok.Data;

@Data
public class NelLog {
    public String action;
    public String mid;
    public String ip;
    public Long cost;
    public String info;

    public NelLog(){};
    public NelLog(String action,String mid,String ip){
        this.action = action;
        this.mid = mid;
        this.ip = ip;
    }
}
