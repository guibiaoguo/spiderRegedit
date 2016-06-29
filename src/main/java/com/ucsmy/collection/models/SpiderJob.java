package com.ucsmy.collection.models;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Administrator on 2015/12/7.
 */
@Entity
@Table(name = "spider_job")
public class SpiderJob extends BaseModel{

    private String uuid;

    private Long jobId;

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
}
