package com.codaconsultancy.cclifeline.repositories;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SpringBootTest(classes = Address.class)
public class AddressRepositoryTest extends BaseTest {

    @Autowired
    private AddressRepository addressRepository;

    private Member member;

    private Address address1;
    private Address address2;

    @Before
    public void setUp() throws Exception {
        member = TestHelper.newMember(5566L, "Jim", "Saunders", "jimbo@email.com", "01383 226655", "0778 866 5544", "Monthly", "Lifeline", "New member", "Open");
        entityManager.persist(member);

        address1 = new Address();
        address1.setAddressLine1("224 Bridge Street");
        address1.setAddressLine2("Pitcorthie");
        address1.setTown("Dunfermline");
        address1.setRegion("Fife");
        address1.setPostcode("KY12 9ZZ");
        address1.setIsActive(false);
        address1.setMember(member);

        address2 = new Address();
        address2.setAddressLine1("1 High Street");
        address2.setTown("Dunfermline");
        address2.setRegion("Fife");
        address2.setPostcode("KY12 9ZZ");
        address2.setIsActive(true);
        address2.setMember(member);

        entityManager.persist(address1);
        entityManager.persist(address2);
    }

    @After
    public void tearDown() throws Exception {
        entityManager.remove(address1);
        entityManager.remove(address2);
        entityManager.remove(member);
    }

    @Test
    public void findByMember() throws Exception {
        List<Address> addressesForMember = addressRepository.findByMember(member);
        assertEquals(2, addressesForMember.size());
        Address firstAddress = addressesForMember.get(0);
        Address secondAddress = addressesForMember.get(1);
        assertNotEquals(firstAddress.getIsActive(), secondAddress.getIsActive());
        assertNotEquals(firstAddress.getAddressLine1(), secondAddress.getAddressLine1());
        assertEquals("Dunfermline", firstAddress.getTown());
        assertEquals("Dunfermline", secondAddress.getTown());
    }

    @Test
    public void findActiveAddressesByMember() throws Exception {
        List<Address> addressesForMember = addressRepository.findByMemberAndIsActive(member, true);
        assertEquals(1, addressesForMember.size());
    }

}