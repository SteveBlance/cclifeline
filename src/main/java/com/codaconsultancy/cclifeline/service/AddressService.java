package com.codaconsultancy.cclifeline.service;

import com.codaconsultancy.cclifeline.domain.Address;
import com.codaconsultancy.cclifeline.repositories.AddressRepository;
import org.springframework.stereotype.Service;

@Service
public class AddressService extends LifelineService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address saveAddress(Address address) {
        return addressRepository.save(address);
    }
}
