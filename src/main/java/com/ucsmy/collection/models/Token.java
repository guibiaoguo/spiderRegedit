package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;

/**
 * Created by Administrator on 2015/10/30.
 */
@Entity
@Table(name = "token")
public class Token extends BaseModel{

    @JsonIgnore
    @OneToOne(mappedBy = "token")
    private Job job;

    private boolean flag;
    private String token;
    @Column(name = "token_url")
    private String tokenUrl;
    @Column(name = "check_token")
    private String checkToken;

    public Token(boolean flag, String token, String tokenUrl, String checkToken) {
        this.flag = flag;
        this.token = token;
        this.tokenUrl = tokenUrl;
        this.checkToken = checkToken;
    }

    public Token(){

    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public void setTokenUrl(String tokenUrl) {
        this.tokenUrl = tokenUrl;
    }

    public String getCheckToken() {
        return checkToken;
    }

    public void setCheckToken(String checkToken) {
        this.checkToken = checkToken;
    }
}
