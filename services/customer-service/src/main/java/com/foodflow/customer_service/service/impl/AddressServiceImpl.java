package com.foodflow.customer_service.service.impl;

import com.foodflow.customer_service.dto.AddressRequest;
import com.foodflow.customer_service.dto.AddressResponse;
import com.foodflow.customer_service.entity.Address;
import com.foodflow.customer_service.entity.Customer;
import com.foodflow.customer_service.exceptions.ResourceNotFoundException;
import com.foodflow.customer_service.repository.AddressRepository;
import com.foodflow.customer_service.repository.CustomerRepository;
import com.foodflow.customer_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl  implements AddressService {

    private final AddressRepository addressRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public AddressResponse addAddress(Long userId, AddressRequest request) {

        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        if(request.getIsDefault()){
            Address address = addressRepository.findByCustomerAndIsDefaultTrue(customer);

            if(address != null){
                address.setIsDefault(false);
                addressRepository.save(address);
            }
        }
        Address address = Address.builder()
                .type(request.getType())
                .customer(customer)
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .zipcode(request.getZipcode())
                .label(request.getLabel())
                .landmark(request.getLandmark())
                .isDefault(request.getIsDefault())
                .build();

        Address savedAddress = addressRepository.save(address);
        return AddressResponse.builder()
                .id(savedAddress.getId())
                .address(savedAddress.getAddress())
                .city(savedAddress.getCity())
                .state(savedAddress.getState())
                .zipcode(savedAddress.getZipcode())
                .label(savedAddress.getLabel())
                .type(savedAddress.getType())
                .landmark((savedAddress.getLandmark()))
                .isDefault(savedAddress.getIsDefault())
                .build();
    }

    @Override
    public void makeDefaultAddress(Long userId, String addressId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Address address = addressRepository.findByCustomerAndId(customer, Long.valueOf(addressId))
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        Address defaultAddress = addressRepository.findByCustomerAndIsDefaultTrue(customer);
        if(defaultAddress != null){
            defaultAddress.setIsDefault(false);
        }

        address.setIsDefault(true);
        addressRepository.save(address);
    }

    @Override
    public AddressResponse updatedAddress(Long userId, String addressId, AddressRequest request) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        Address address = addressRepository.findByCustomerAndId(customer, Long.valueOf(addressId))
                .orElseThrow(() -> new ResourceNotFoundException("Address not found"));

        address.setAddress(request.getAddress());
        address.setCity(request.getCity());
        address.setState(request.getCity());
        address.setLabel(request.getLabel());
        address.setZipcode(request.getZipcode());
        address.setIsDefault(request.getIsDefault());

        Address savedAddress = addressRepository.save(address);

        return AddressResponse.builder()
                .id(savedAddress.getId())
                .address(savedAddress.getAddress())
                .city(savedAddress.getCity())
                .state(savedAddress.getState())
                .zipcode(savedAddress.getZipcode())
                .label(savedAddress.getLabel())
                .isDefault(savedAddress.getIsDefault())
                .build();
    }

    @Override
    public List<AddressResponse> getAddress(Long userId) {
        Customer customer = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        List<Address> addressList = addressRepository.findByCustomer(customer);

        return addressList.stream()
                .map(address -> AddressResponse.builder()
                        .id(address.getId())
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipcode(address.getZipcode())
                        .label(address.getLabel())
                        .isDefault(address.getIsDefault())
                        .lat(address.getLatitude())
                        .lng(address.getLongitude())
                        .build())
                .toList();
    }
}
