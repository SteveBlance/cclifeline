package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Payment;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PaymentViewBean {

    public PaymentViewBean() {
    }

    public PaymentViewBean(Date paymentDate, Float paymentAmount, String creditReference, String creditedAccount) {
        this.paymentDate = paymentDate;
        this.paymentAmount = paymentAmount;
        this.creditReference = creditReference;
        this.creditedAccount = creditedAccount;
    }

    private Long id;

    @NotNull
    private Date paymentDate;

    @NotNull
    private Float paymentAmount;

    @NotNull
    private String creditReference;

    private String creditedAccount;

    private Long memberId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getCreditedAccount() {
        return creditedAccount;
    }

    public void setCreditedAccount(String creditedAccount) {
        this.creditedAccount = creditedAccount;
    }

    public String getCreditReference() {
        return creditReference;
    }

    public void setCreditReference(String creditReference) {
        this.creditReference = creditReference;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public Payment toEntity() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(this, Payment.class);
    }
}

