package com.foodflow.customer_service.service;

import com.foodflow.customer_service.dto.AddNewCustomerRequest;
import com.foodflow.customer_service.dto.CustomerHomeResponse;
import com.foodflow.customer_service.dto.CustomerProfileResponseDto;
import com.foodflow.customer_service.dto.UpdateCustomerProfileRequest;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerService {
    CustomerProfileResponseDto getMyProfile(Long userId);

    void updateProfile(Long userId, UpdateCustomerProfileRequest request);

    String updateUserImage(Long userId, MultipartFile image);


    CustomerHomeResponse home(Long userId);

    void addNewCustomer(AddNewCustomerRequest request);
}
