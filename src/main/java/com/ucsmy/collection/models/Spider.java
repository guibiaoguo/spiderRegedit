package com.ucsmy.collection.models;

import javax.persistence.*;

/**
 * Created by Bill on 2015/11/15.
 */
@Entity
@Table(name = "spider")
public class Spider extends BaseModel{

    @Column(unique = true,name = "spider_name")
    private String spiderName;

    private String uuid;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "nike_name")
    private String nikeName;

    private int status;

    private double version;

    @ManyToOne
    private Job job;

    @ManyToOne
    private TaskServer taskServer;

    @Column(length = 4000)
    private String info;

    private String mac;

    private String agentID;

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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public TaskServer getTaskServer() {
        return taskServer;
    }

    public void setTaskServer(TaskServer taskServer) {
        this.taskServer = taskServer;
    }

    public String getSpiderName() {
        return spiderName;
    }

    public Spider(String spiderName, String uuid, String clientIp, String nikeName, int status) {
        this.spiderName = spiderName;
        this.uuid = uuid;
        this.clientIp = clientIp;
        this.nikeName = nikeName;
        this.status = status;
    }

    public Spider() {

    }

    public void setSpiderName(String spiderName) {
        this.spiderName = spiderName;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }
}
