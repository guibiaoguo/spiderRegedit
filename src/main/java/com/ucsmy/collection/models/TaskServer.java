package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */

@Entity
@Table(name = "task_server")
public class TaskServer extends BaseModel{

    @Column(name = "server_name")
    private String serverName;

    private String ip;

    private String domain;

    @Column(name = "spider_count")
    private Integer spiderCount;

    @JsonIgnore
    @OneToMany(mappedBy = "taskServer",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Spider> spiders;

    @JsonIgnore
    @OneToMany(mappedBy = "taskServer",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<SpiderLog> spiderLogs;

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getSpiderCount() {
        return spiderCount;
    }

    public void setSpiderCount(Integer spiderCount) {
        this.spiderCount = spiderCount;
    }

    public List<Spider> getSpiders() {
        return spiders;
    }

    public void setSpiders(List<Spider> spiders) {
        this.spiders = spiders;
    }

    public List<SpiderLog> getSpiderLogs() {
        return spiderLogs;
    }

    public void setSpiderLogs(List<SpiderLog> spiderLogs) {
        this.spiderLogs = spiderLogs;
    }
}
