package com.ucsmy.collection.models;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/12/30.
 */

@Entity
@Table(name = "data_msg")
public class DataMsg extends BaseModel{

    private String name;

    private int count;

    private String info;

    private boolean status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Column(length = 16777216)
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
