package com.foodflow.customer_service.service;

import com.foodflow.customer_service.dto.AddressRequest;
import com.foodflow.customer_service.dto.AddressResponse;

import java.util.List;

public interface AddressService {

    AddressResponse addAddress(Long userId, AddressRequest request);

    void makeDefaultAddress(Long userId, String addressId);

    AddressResponse updatedAddress(Long userId, String addressId, AddressRequest request);

    List<AddressResponse> getAddress(Long userId);
}
