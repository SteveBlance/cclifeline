package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.common.TestHelper;
import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.domain.Member;
import com.codaconsultancy.cclifeline.repositories.AddressRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@EntityScan("com.codaconsultancy.cclifeline.domain")
@SpringBootTest(classes = AddressService.class)
public class AddressServiceTest {

    @Autowired
    private AddressService addressService;

    @MockBean
    AddressRepository addressRepository;

    @Test
    public void saveAddress() throws Exception {

        Address address = new Address();
        Member member = TestHelper.newMember(1234L, "Bob", "Beth", "bb@email.com", null, null, "Monthly", "Lifeline", null, "Open");
//        address.setMember(member);
        address.setAddressLine1("24 High Street, Saline");
        address.setAddressLine2("Flat 6");
        address.setAddressLine3("Upper");
        address.setTown("Dunfermline");
        address.setRegion("Fife");
        address.setPostcode("KY12 8YY");
        address.setIsActive(true);

        when(addressRepository.save(address)).thenReturn(address);

        Address savedAddress = addressService.saveAddress(address);

        verify(addressRepository, times(1)).save(address);

        assertSame(address, savedAddress);

    }

}