package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

/**
 * Created by Administrator on 2015/10/14.
 */
@Entity
@Table(name = "proxy")
public class Proxy extends BaseModel{

    @Column(name = "proxy_host")
    private String proxyHost;
    @Column(name="proxy_port")
    private String proxyPort;
    @Column(name="user_naem")
    private String userName;

    private String password;

    private int status = 0;

    public Proxy(String proxyHost, String proxyPort, String userName, String password) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.userName = userName;
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Proxy(){

    }

    public String getProxyHost() {
        return proxyHost;
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
