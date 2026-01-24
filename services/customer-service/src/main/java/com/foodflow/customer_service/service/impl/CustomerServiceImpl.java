package com.foodflow.customer_service.service.impl;

import com.foodflow.customer_service.client.OrderServiceClient;
import com.foodflow.customer_service.client.RestaurantServiceClient;
import com.foodflow.customer_service.dto.*;
import com.foodflow.customer_service.entity.Address;
import com.foodflow.customer_service.entity.Customer;
import com.foodflow.customer_service.exceptions.ResourceNotFoundException;
import com.foodflow.customer_service.repository.AddressRepository;
import com.foodflow.customer_service.repository.CustomerRepository;
import  com.foodflow.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderServiceClient orderServiceClient;
    private final AddressRepository addressRepository;
    private final RestaurantServiceClient restaurantServiceClient;

    @Override
    public CustomerProfileResponseDto getMyProfile(Long userId) {
        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Address> addressList = addressRepository.findByCustomer(myProfile);

        List<AddressResponse> addressResponsesList = addressList.stream().map(
                address -> AddressResponse.builder()
                        .id(address.getId())
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .label(address.getLabel())
                        .zipcode(address.getZipcode())
                        .isDefault(address.getIsDefault())
                        .build()
        ).toList();

        CustomerOrderStatsResponse orderStatsResponse = orderServiceClient.getUserOrderStats(myProfile.getUserId());

        return CustomerProfileResponseDto.builder()
                .userId(myProfile.getUserId())
                .name(myProfile.getName())
                .phone(myProfile.getPhone())
                .email(myProfile.getEmail())
                .profileImageUrl(myProfile.getProfileImageUrl())
                .totalOrders((int) orderStatsResponse.getTotalOrders())
                .activeOrders((int) orderStatsResponse.getTotalOrders())
                .cancelledOrders((int) orderStatsResponse.getTotalOrders())
                .joinedAt(myProfile.getCreatedAt())
                .address(addressResponsesList)
                .build();
    }

    @Override
    public CustomerProfileResponseDto updateProfile(UpdateCustomerProfileRequest request) {
        return null;
    }

    @Override
    public void updateUserImage(MultipartFile image) {

    }

    @Override
    public CustomerHomeResponse home(Long userId) {
        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        List<Address> myAddressList = addressRepository.findByCustomer(myProfile);

        List<AddressResponse> addressResponsesList = myAddressList.stream().map(
                address -> AddressResponse.builder()
                        .id(address.getId())
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .label(address.getLabel())
                        .zipcode(address.getZipcode())
                        .isDefault(address.getIsDefault())
                        .build()
            ).toList();

        String city = myAddressList.stream().filter(Address::getIsDefault).map(Address::getCity).findFirst().orElse(null);

        if(city == null){
            city = "Gurugram";
        }

        List<RestaurantCardDto> restaurantList =  restaurantServiceClient.getRestaurantByCity(city);

        CartPreviewDto myCartPreview = orderServiceClient.getMyCartPreview(userId);

        return CustomerHomeResponse.builder()
                .address(addressResponsesList)
                .cartPreview(myCartPreview)
                .restaurants(restaurantList)
                .build();
    }

    @Override
    public void addNewCustomer(AddNewCustomerRequest request) {
        Customer customer = Customer.builder()
                .phone(request.getPhone())
                .email(request.getEmail())
                .userId(request.getUserId())
                .build();

        customerRepository.save(customer);
    }
}
