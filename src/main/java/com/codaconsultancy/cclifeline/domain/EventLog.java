package com.codaconsultancy.cclifeline.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "EVENT_LOG")
public class EventLog {

    private EventLog() {
    }

    public EventLog(String message) {
        this.message = message;
    }

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "MESSAGE")
    private String message;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
