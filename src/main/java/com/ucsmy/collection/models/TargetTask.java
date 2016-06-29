package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/11/6.
 */
@Entity
@Table(name = "target_task")
public class TargetTask extends BaseModel{

    @Column(name = "target_url")
    private String targetUrl;
    private String method;
    private String express;
    @Column(name = "_key")
    private String key;

    private Boolean flag;

    @Column(name = "is_most")
    private Boolean isMost;

    @JsonIgnore
    @ManyToOne
    private Tasks tasks;

    @Column(name = "save_table")
    private String saveTable;

    public TargetTask(String targetUrl, String method, String express, String key, Boolean flag, Boolean isMost) {
        this.targetUrl = targetUrl;
        this.method = method;
        this.express = express;
        this.key = key;
        this.flag = flag;
        this.isMost = isMost;
    }

    public TargetTask() {

    }

    public String getSaveTable() {
        return saveTable;
    }

    public void setSaveTable(String saveTable) {
        this.saveTable = saveTable;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public Boolean getIsMost() {
        return isMost;
    }

    public void setIsMost(Boolean isMost) {
        this.isMost = isMost;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }
}
