package com.heavens.stream.models;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@MappedSuperclass
public class Actionable implements Serializable {

    @Column(name = "string_id", nullable = false, unique = true)
    protected String stringId = UUID.randomUUID().toString();

    @Column(name = "active", columnDefinition= "boolean default true")
    protected boolean active =true;

    @CreationTimestamp
    @Column(name = "date_created")
    protected Date dateCreated = new Date();

    @UpdateTimestamp
    @Column(name = "date_updated")
    protected Date dateUpdated = new Date();

    @Column(name = "updated_by")
    protected Long updatedBy = 0L;

    @Column(name = "created_by")
    protected Long createdBy = 0L;

    @PrePersist
    public void prePersist() {
        if (dateCreated == null) {
            dateCreated = new Date();
        }
    }

    @PreUpdate
    public void preUpdate() {
        dateUpdated = new Date();
    }

    public void actionedBy(Long id){
        this.createdBy = id;
        this.updatedBy = id;
    }

    public void actionedByWithStringID(Long id, String stringId) {
        this.createdBy = id;
        this.updatedBy = id;
        this.stringId = stringId;
    }

    @Override
    public String toString() {
        return "Actionable{" +
                "stringId='" + stringId + '\'' +
                ", active=" + active +
                ", dateCreated=" + dateCreated +
                ", dateUpdated=" + dateUpdated +
                ", updatedBy=" + updatedBy +
                ", createdBy=" + createdBy +
                '}';
    }
}
