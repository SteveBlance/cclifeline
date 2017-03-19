package com.codaconsultancy.domain;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class AddressTest {

    private Address address;

    @Before
    public void setUp() {
        address = new Address();
        address.setAddressLine1("Line1");
        address.setAddressLine2("Line2");
        address.setAddressLine3("Line3");
        address.setTown("Dunfermline");
        address.setRegion("Fife");
        address.setPostcode("KY12 9AB");
        address.setIsActive(true);
        Member member = new Member();
        member.setForename("Hamish");
        member.setSurname("Petrie");
        address.setMember(member);
    }

    @Test
    public void getAddressLine1() {
        Assert.assertEquals("Line1", address.getAddressLine1());
    }

    @Test
    public void getAddressLine2() {

    }

    @Test
    public void getAddressLine3() {

    }

    @Test
    public void getTown() {

    }

    @Test
    public void getRegion() {

    }

    @Test
    public void getPostcode() {

    }

    @Test
    public void getIsActive() {

    }

    @Test
    public void getMember() {

    }

}