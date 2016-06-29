package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 */
@Entity
@Table(name = "tasks")
public class Tasks extends BaseModel {
    @Column(name = "source_table")
    private String sourceTable;
    @Column(name="save_table")
    private String saveTable;
    private int page;
    @Column(name = "page_size")
    private int pageSize;
    @Column(name = "content_type")
    private int contentType;

    @JsonIgnore
    @OneToOne(mappedBy = "tasks")
    private Job job;

    @JsonIgnore
    @OneToMany(mappedBy = "tasks")
    private List<TargetTask> targetTasks = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "tasks")
    private List<HelpTask> helpTasks = new ArrayList<>();

    private int count = 8;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Tasks(String sourceTable, String saveTable, int page, int pageSize, int contentType) {
        this.sourceTable = sourceTable;
        this.saveTable = saveTable;
        this.page = page;
        this.pageSize = pageSize;
        this.contentType = contentType;
    }

    public Tasks() {

    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getSaveTable() {
        return saveTable;
    }

    public void setSaveTable(String saveTable) {
        this.saveTable = saveTable;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getContentType() {
        return contentType;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public List<TargetTask> getTargetTasks() {
        return targetTasks;
    }

    public void setTargetTasks(List<TargetTask> targetTasks) {
        this.targetTasks = targetTasks;
    }

    public List<HelpTask> getHelpTasks() {
        return helpTasks;
    }

    public void setHelpTasks(List<HelpTask> helpTasks) {
        this.helpTasks = helpTasks;
    }
}
