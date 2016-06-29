package com.ucsmy.collection.models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2016/1/8.
 */
@Entity
@Table(name = "spider_application")
public class SpiderApplicaton extends BaseModel{

    private String name;
    private double version;
    private String url;
    private String info;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
