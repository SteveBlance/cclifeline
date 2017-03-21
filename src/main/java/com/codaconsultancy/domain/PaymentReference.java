package com.codaconsultancy.domain;

import javax.persistence.*;

@Entity
@Table(name = "PAYMENT_REFERENCES")
public class PaymentReference {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "REFERENCE")
    private String reference;

    @Column(name = "IS_ACTIVE")
    private Boolean isActive;

    @ManyToOne
    private Member member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
