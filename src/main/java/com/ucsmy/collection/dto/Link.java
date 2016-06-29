package com.ucsmy.collection.dto;

import java.util.*;

/**
 * Created by Bill on 2016/1/10.
 */
public class Link {

    private String sourceLink;
    private Set<Map> links = new HashSet<>();

    public String getSourceLink() {
        return sourceLink;
    }

    public void setSourceLink(String sourceLink) {
        this.sourceLink = sourceLink;
    }

    public Set<Map> getLinks() {
        return links;
    }

    public void setLinks(Set<Map> links) {
        this.links = links;
    }
}
