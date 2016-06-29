package com.ucsmy.collection.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * An abstract base model class for entities
 *
 * @author Raysmond<i@raysmond.com>
 */
@MappedSuperclass
public abstract class BaseModel implements Comparable<BaseModel>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonIgnore
    @Column(name = "create_time",nullable = false)
    private Date createdTime;

    @JsonIgnore
    @Column(name = "update_time",nullable = false)
    private Date updatedTime;

    @PrePersist
    public void prePersist(){
        createdTime = updatedTime = new Date();
    }

    @PreUpdate
    public void preUpdate(){
        updatedTime = new Date();
    }

    @Override
    public int compareTo(BaseModel o) {
        return this.getId().compareTo(o.getId());
    }

    public boolean equals(Object other) {
        if (other == null || other.getClass() != this.getClass())
            return false;

        return this.getId().equals(((BaseModel) other).getId());
    }


    public int hashCode() {
        return new HashCodeBuilder().append(getId()).toHashCode();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long _id) {
        id = _id;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

}