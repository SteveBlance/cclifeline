package com.codaconsultancy.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "PAYMENTS")
public class Payment {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "PAYMENT_DATE")
    private Date paymentDate;

    @Column(name = "PAYMENT_AMOUNT")
    private Float paymentAmount;

    @ManyToOne
    private Member member;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(Float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
