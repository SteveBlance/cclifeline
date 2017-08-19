package com.codaconsultancy.cclifeline.view;

import com.codaconsultancy.cclifeline.domain.Member;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddressViewBeanTest {

    private AddressViewBean addressViewBean;

    @Before
    public void setUp() {
        addressViewBean = new AddressViewBean();
        addressViewBean.setId(86L);
        addressViewBean.setAddressLine1("Line1");
        addressViewBean.setAddressLine2("Line2");
        addressViewBean.setAddressLine3("Line3");
        addressViewBean.setTown("Dunfermline");
        addressViewBean.setRegion("Fife");
        addressViewBean.setPostcode("KY12 9AB");
        addressViewBean.setIsActive(true);
        Member member = new Member();
        member.setForename("Hamish");
        member.setSurname("Petrie");
        addressViewBean.setMemberId(23L);
    }

    @Test
    public void getId() {
        assertEquals(86L, addressViewBean.getId().longValue());
        assertEquals(86L, addressViewBean.toEntity().getId().longValue());
    }

    @Test
    public void getAddressLine1() {
        assertEquals("Line1", addressViewBean.getAddressLine1());
        assertEquals("Line1", addressViewBean.toEntity().getAddressLine1());
    }

    @Test
    public void getAddressLine2() {
        assertEquals("Line2", addressViewBean.getAddressLine2());
    }

    @Test
    public void getAddressLine3() {
        assertEquals("Line3", addressViewBean.getAddressLine3());
    }

    @Test
    public void getTown() {
        assertEquals("Dunfermline", addressViewBean.getTown());
    }

    @Test
    public void getRegion() {
        assertEquals("Fife", addressViewBean.getRegion());
    }

    @Test
    public void getPostcode() {
        assertEquals("KY12 9AB", addressViewBean.getPostcode());
    }

    @Test
    public void getIsActive() {
        Assert.assertTrue(addressViewBean.getIsActive());
    }

    @Test
    public void getMemberId() {
        Long memberId = addressViewBean.getMemberId();
        assertEquals(23L, memberId.longValue());
    }

    @Test
    public void getFormattedAddress() {
        assertEquals("Line1<br/>Line2<br/>Line3<br/>Dunfermline<br/>Fife<br/>KY12 9AB", addressViewBean.getFormattedAddress());
        addressViewBean.setAddressLine3("");
        assertEquals("Line1<br/>Line2<br/>Dunfermline<br/>Fife<br/>KY12 9AB", addressViewBean.getFormattedAddress());

    }

}