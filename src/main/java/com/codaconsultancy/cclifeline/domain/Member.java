package com.codaconsultancy.cclifeline.domain;

import com.codaconsultancy.cclifeline.view.MemberViewBean;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "MEMBERS")
public class Member {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull()
    @Column(name = "MEMBERSHIP_NUMBER")
    private Long membershipNumber;

    @NotNull
    @Column(name = "MEMBERSHIP_TYPE")
    private String membershipType;

    @NotNull
    @Column(name = "STATUS")
    private String status;

    @NotNull
    @Column(name = "FORENAME")
    private String forename;

    @NotNull
    @Column(name = "SURNAME")
    private String surname;

    @NotNull
    @Column(name = "PAYER_TYPE")
    private String payerType;

    @Column(name = "JOIN_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date joinDate;

    @Column(name = "LEAVE_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date leaveDate;

    @Column(name = "COMMENTS")
    private String comments;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "LANDLINE_NUMBER")
    private String landlineNumber;

    @Column(name = "MOBILE_NUMBER")
    private String mobileNumber;

    @Column(name = "CARD_REQUEST_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date cardRequestDate;

    @Column(name = "CARD_ISSUED_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date cardIssuedDate;

    @Column(name = "WELCOME_LETTER_ISSUED_DATE")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date welcomeLetterIssuedDate;

    @OneToMany(mappedBy = "member")
    private List<Address> addresses = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMembershipType() {
        return membershipType;
    }

    public void setMembershipType(String membershipType) {
        this.membershipType = membershipType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getForename() {
        return forename;
    }

    public void setForename(String forename) {
        this.forename = forename;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPayerType() {
        return payerType;
    }

    public void setPayerType(String payerType) {
        this.payerType = payerType;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public Date getLeaveDate() {
        return leaveDate;
    }

    public void setLeaveDate(Date leaveDate) {
        this.leaveDate = leaveDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLandlineNumber() {
        return landlineNumber;
    }

    public void setLandlineNumber(String landlineNumber) {
        this.landlineNumber = landlineNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setMembershipNumber(Long membershipNumber) {
        this.membershipNumber = membershipNumber;
    }

    public Long getMembershipNumber() {
        return membershipNumber;
    }

    public Date getCardRequestDate() {
        return cardRequestDate;
    }

    public void setCardRequestDate(Date cardRequestDate) {
        this.cardRequestDate = cardRequestDate;
    }

    public Date getCardIssuedDate() {
        return cardIssuedDate;
    }

    public void setCardIssuedDate(Date cardIssuedDate) {
        this.cardIssuedDate = cardIssuedDate;
    }

    public Date getWelcomeLetterIssuedDate() {
        return welcomeLetterIssuedDate;
    }

    public void setWelcomeLetterIssuedDate(Date welcomeLetterIssuedDate) {
        this.welcomeLetterIssuedDate = welcomeLetterIssuedDate;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public MemberViewBean toViewBean() {
        MapperFactory mapperFactory = new DefaultMapperFactory.Builder().build();
        MapperFacade mapper = mapperFactory.getMapperFacade();
        return mapper.map(this, MemberViewBean.class);
    }
}
