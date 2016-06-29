package com.ucsmy.collection.models;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/11/6.
 */
@Entity
@Table(name = "help_task")
public class HelpTask extends BaseModel{

    @JsonIgnore
    @ManyToOne
    private Tasks tasks;

    @Column(name = "http_method")
    private String httpMethod;

    @Column(name = "help_url")
    private String helpUrl;

//    @JsonIgnore
//    @OneToMany(targetEntity = Parame.class,fetch = FetchType.LAZY,cascade = CascadeType.ALL,mappedBy = "helpTask")
//    @MapKey(name = "columnName")
//    private Map parame = new HashedMap();
//
//    public Map getParame() {
//        return parame;
//    }
//
//    public void setParame(Map parame) {
//        this.parame = parame;
//    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="param",
            joinColumns=@JoinColumn(name="id"))
    @Column(name="column_value")
    @MapKeyColumn(name = "column_name")
    private Map<String,String> params = new HashMap<>();

    @Column(name = "right_code")
    private String rightCode;

    public Tasks getTasks() {
        return tasks;
    }

    public void setTasks(Tasks tasks) {
        this.tasks = tasks;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public String getHelpUrl() {
        return helpUrl;
    }

    public void setHelpUrl(String helpUrl) {
        this.helpUrl = helpUrl;
    }

    public String getRightCode() {
        return rightCode;
    }

    public void setRightCode(String rightCode) {
        this.rightCode = rightCode;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }
}

