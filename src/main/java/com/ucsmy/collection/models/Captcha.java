package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/10/30.
 */
@Entity
@Table(name = "captcha")
public class Captcha extends BaseModel{

    @JsonIgnore
    @OneToOne(mappedBy = "captcha",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Job job;

    private String code;

    private boolean flag;

    @Column(name = "code_url")
    private String codeUrl;

    @Column(name = "right_code")
    private String rightCode;

    @Column(name = "cap_url")
    private String capUrl;

    public Captcha(String code, boolean flag, String codeUrl, String rightCode, String capUrl) {
        this.code = code;
        this.flag = flag;
        this.codeUrl = codeUrl;
        this.rightCode = rightCode;
        this.capUrl = capUrl;
    }

    public Captcha(){

    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getCodeUrl() {
        return codeUrl;
    }

    public void setCodeUrl(String codeUrl) {
        this.codeUrl = codeUrl;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public String getCapUrl() {
        return capUrl;
    }

    public void setCapUrl(String capUrl) {
        this.capUrl = capUrl;
    }
}
