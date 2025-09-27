package com.address.service;

import com.address.model.dto.AddressDto;
import com.address.model.dto.AddressRequest;

import java.util.List;

public interface AddressService {

    List<AddressDto> saveAddress(AddressRequest addressRequest);

    List<AddressDto> updateAddress(AddressRequest addressRequest);

    AddressDto getSingleAddress(Long id);

    List<AddressDto> getAllAddress();

    void deleteAddress(Long id);
}
