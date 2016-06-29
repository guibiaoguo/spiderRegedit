package com.ucsmy.collection.models;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.*;

/**
 * Created by Administrator on 2015/10/13.
 */
@Entity
@Table(name = "job")
public class Job extends BaseModel{

    @Column(name="dst_url")
    private String dstUrl;

    private String name;
    private int status;
    @Column(name = "is_authentication")
    private Boolean isAuthentication;
    @Column(name = "auth_group")
    private String authGroup;

    @Column(name = "content_type")
    private int contentType;

    @JsonIgnore
    @OneToMany(mappedBy = "job")
    private List<Cookie> cookies = new ArrayList<>();

    @OneToOne
    private Captcha captcha;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="header",
            joinColumns=@JoinColumn(name="id"))
    @Column(name="column_value")
    @MapKeyColumn(name = "column_name")
    private Map<String,String> headers = new HashMap<>();

    @OneToOne
    private Tasks tasks;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="login",
            joinColumns=@JoinColumn(name="id"))
    @Column(name="column_value")
    @MapKeyColumn(name = "column_name")
    private Map<String,String> logins = new HashMap<>();

    @OneToOne
    private Token token;

    @OneToOne
    private RedisConfig redisConfig;

    private String domain;
    private String charset = "UTF-8";
    @Column(name = "user_agent")
    private String userAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:35.0) Gecko/20100101 Firefox/35.0";

    @Column(name = "login_url")
    private String loginUrl;

    private String scheduler;

    @Column(name="sleep_time")
    private int sleepTime = 5000;

    @Column(name="retry_times")
    private int retryTimes = 0;

    @Column(name="cycle_retry_times")
    private int cycleRetryTimes = 0;

    @Column(name = "sleep_random")
    private boolean sleepRandom;

    private int thread = 2;

    private int timeOut = 5000;

    @Column(name = "job_type")
    private int jobType = 0;

    @JsonIgnore
    @OneToMany(mappedBy = "job",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Spider> spiders;

    private boolean clearClient;

    public Job(String dstUrl, String name, int status, Boolean isAuthentication, String authGroup, int contentType, String domain, String charset, String userAgent, String loginUrl, String scheduler, int sleepTime, int retryTimes, int cycleRetryTimes, boolean sleepRandom, int thread) {
        this.dstUrl = dstUrl;
        this.name = name;
        this.status = status;
        this.isAuthentication = isAuthentication;
        this.authGroup = authGroup;
        this.contentType = contentType;
        this.domain = domain;
        this.charset = charset;
        this.userAgent = userAgent;
        this.loginUrl = loginUrl;
        this.scheduler = scheduler;
        this.sleepTime = sleepTime;
        this.retryTimes = retryTimes;
        this.cycleRetryTimes = cycleRetryTimes;
        this.sleepRandom = sleepRandom;
        this.thread = thread;
    }

    public Job() {

    }

    public boolean isClearClient() {
        return clearClient;
    }

    public void setClearClient(boolean clearClient) {
        this.clearClient = clearClient;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public String getDstUrl() {
        return dstUrl;
    }

    public void setDstUrl(String dstUrl) {
        this.dstUrl = dstUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Boolean getIsAuthentication() {
        return isAuthentication;
    }

    public void setIsAuthentication(Boolean isAuthentication) {
        this.isAuthentication = isAuthentication;
    }

    public String getAuthGroup() {
        return authGroup;
    }

    public void setAuthGroup(String authGroup) {
        this.authGroup = authGroup;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Captcha getCaptcha() {
        return captcha;
    }

    public void setCaptcha(Captcha captcha) {
        this.captcha = captcha;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }


    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getScheduler() {
        return scheduler;
    }

    public void setScheduler(String scheduler) {
        this.scheduler = scheduler;
    }

    public int getSleepTime() {
        return sleepTime;
    }

    public void setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public int getCycleRetryTimes() {
        return cycleRetryTimes;
    }

    public void setCycleRetryTimes(int cycleRetryTimes) {
        this.cycleRetryTimes = cycleRetryTimes;
    }

    public boolean isSleepRandom() {
        return sleepRandom;
    }

    public void setSleepRandom(boolean sleepRandom) {
        this.sleepRandom = sleepRandom;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public List<Spider> getSpiders() {
        return spiders;
    }

    public void setSpiders(List<Spider> spiders) {
        this.spiders = spiders;
    }

    public int getJobType() {
        return jobType;
    }

    public void setJobType(int jobType) {
        this.jobType = jobType;
    }

    public RedisConfig getRedisConfig() {
        return redisConfig;
    }

    public void setRedisConfig(RedisConfig redisConfig) {
        this.redisConfig = redisConfig;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public Map<String, String> getLogins() {
        return logins;
    }

    public void setLogins(Map<String, String> logins) {
        this.logins = logins;
    }

    public List<Cookie> getCookies() {
        return cookies;
    }

    public void setCookies(List<Cookie> cookies) {
        this.cookies = cookies;
    }
}
