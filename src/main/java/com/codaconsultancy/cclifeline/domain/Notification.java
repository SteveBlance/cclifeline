package com.codaconsultancy.cclifeline.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "NOTIFICATIONS")
public class Notification {

    public Notification() {
    }

    public Notification(Date eventDate, String eventType, String description) {
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.description = description;
    }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Column(name = "EVENT_DATE")
    private Date eventDate;

    @NotNull
    @Column(name = "EVENT_TYPE")
    private String eventType;

    @Column(name = "DESCRIPTION")
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
