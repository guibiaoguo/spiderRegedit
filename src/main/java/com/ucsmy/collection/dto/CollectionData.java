package com.ucsmy.collection.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingDeque;

/**
 * Created by Administrator on 2015/12/2.
 */
public class CollectionData {

    private Map<String,List<Map>> datas = new HashMap<>();
    private Map<String,List<Map>> links = new HashMap<>();
    private List<Map> requests = new ArrayList<>();

    private String sourceLink;

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public Map<String, List<Map>> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, List<Map>> datas) {
        this.datas = datas;
    }

    public Map<String, List<Map>> getLinks() {
        return links;
    }

    public void setLinks(Map<String, List<Map>> links) {
        this.links = links;
    }

    public List<Map> getRequests() {
        return requests;
    }

    public void setRequests(List<Map> requests) {
        this.requests = requests;
    }
}
