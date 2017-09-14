package com.codaconsultancy.cclifeline.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "CONFIGURATION")
public class Configuration {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(name = "NAME")
    private String name;


    @Column(name = "STRING_VALUE")
    private String stringValue;

    @Column(name = "BOOLEAN_VALUE")
    private boolean booleanValue;

    @Column(name = "DATE_VALUE")
    private Date dateValue;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }
}
