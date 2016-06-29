package com.ucsmy.collection.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/12/29.
 */
@Entity
@Table(name = "spider_log")
public class SpiderLog extends BaseModel{

    private String uuid;

    private Long jobId;

    @Column(length = 4000)
    private String info;

    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    private String mac;

    private String agentID;

    @ManyToOne
    private TaskServer taskServer;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public TaskServer getTaskServer() {
        return taskServer;
    }

    public void setTaskServer(TaskServer taskServer) {
        this.taskServer = taskServer;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getAgentID() {
        return agentID;
    }

    public void setAgentID(String agentID) {
        this.agentID = agentID;
    }
}
