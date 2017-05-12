package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.repositories.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    @Autowired
    private AddressRepository addressRepository;

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}
