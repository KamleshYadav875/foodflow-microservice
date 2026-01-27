package com.foodflow.customer_service.service.impl;

import com.foodflow.customer_service.client.MediaServiceClient;
import com.foodflow.customer_service.client.OrderServiceClient;
import com.foodflow.customer_service.client.RestaurantServiceClient;
import com.foodflow.customer_service.dto.*;
import com.foodflow.customer_service.entity.Address;
import com.foodflow.customer_service.entity.Customer;
import com.foodflow.customer_service.exceptions.BadRequestException;
import com.foodflow.customer_service.exceptions.ResourceNotFoundException;
import com.foodflow.customer_service.repository.AddressRepository;
import com.foodflow.customer_service.repository.CustomerRepository;
import  com.foodflow.customer_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderServiceClient orderServiceClient;
    private final AddressRepository addressRepository;
    private final RestaurantServiceClient restaurantServiceClient;
    private final MediaServiceClient mediaServiceClient;

    @Override
    public CustomerProfileResponseDto getMyProfile(Long userId) {
        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<Address> addressList = addressRepository.findByCustomer(myProfile);

        List<AddressResponse> addressResponseList =addressList.stream().map(
                address -> AddressResponse.builder()
                        .id(address.getId())
                        .type(address.getType())
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipcode(address.getZipcode())
                        .isDefault(address.getIsDefault())
                        .label(address.getLabel())
                        .landmark(address.getLandmark())
                        .build()
        ).toList();

        return CustomerProfileResponseDto.builder()
                .userId(myProfile.getUserId())
                .name(myProfile.getName())
                .phone(myProfile.getPhone())
                .email(myProfile.getEmail())
                .profileImageUrl(myProfile.getProfileImageUrl())
                .addresses(addressResponseList)
                .build();
    }

    @Override
    public void updateProfile(Long userId, UpdateCustomerProfileRequest request) {
        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        if(request.getName() != null){
            myProfile.setName(request.getName());
            customerRepository.save(myProfile);
        }
    }

    @Override
    public String updateUserImage(Long userId, MultipartFile image) {
        if(image == null){
            throw new BadRequestException("Please select image");
        }

        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());
        body.add("folder", "customer-profile");
        String url = mediaServiceClient.uploadImage(image.getResource(),"customer-profile");

        myProfile.setProfileImageUrl(url);
        customerRepository.save(myProfile);
        return url;
    }

    @Override
    public CustomerHomeResponse home(Long userId) {
        Customer myProfile = customerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer profile not found"));

        List<Address> myAddressList = addressRepository.findByCustomer(myProfile);

        List<AddressResponse> addressResponsesList = myAddressList.stream().map(
                address -> AddressResponse.builder()
                        .id(address.getId())
                        .type(address.getType())
                        .address(address.getAddress())
                        .city(address.getCity())
                        .state(address.getState())
                        .zipcode(address.getZipcode())
                        .isDefault(address.getIsDefault())
                        .label(address.getLabel())
                        .landmark(address.getLandmark())
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
                .name(request.getName())
                .userId(request.getUserId())
                .build();

        customerRepository.save(customer);
    }
}
