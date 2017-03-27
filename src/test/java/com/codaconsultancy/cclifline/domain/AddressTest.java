package com.codaconsultancy.cclifline.domain;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AddressTest {

    private Address address;

    @Before
    public void setUp() {
        address = new Address();
        address.setId(86L);
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
    public void getId() {
        assertEquals(86L, address.getId().longValue());
    }

    @Test
    public void getAddressLine1() {
        assertEquals("Line1", address.getAddressLine1());
    }

    @Test
    public void getAddressLine2() {
        assertEquals("Line2", address.getAddressLine2());
    }

    @Test
    public void getAddressLine3() {
        assertEquals("Line3", address.getAddressLine3());
    }

    @Test
    public void getTown() {
        assertEquals("Dunfermline", address.getTown());
    }

    @Test
    public void getRegion() {
        assertEquals("Fife", address.getRegion());
    }

    @Test
    public void getPostcode() {
        assertEquals("KY12 9AB", address.getPostcode());
    }

    @Test
    public void getIsActive() {
        Assert.assertTrue(address.getIsActive());
    }

    @Test
    public void getMember() {
        Member member = address.getMember();
        assertEquals("Hamish", member.getForename());
        assertEquals("Petrie", member.getSurname());
    }

}