package com.foodflow.customer_service.controller;

import com.foodflow.customer_service.dto.AddressRequest;
import com.foodflow.customer_service.dto.AddressResponse;
import com.foodflow.customer_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<String> addAddress(@RequestHeader("X-USER-ID") Long userId, @RequestBody AddressRequest request){
        addressService.addAddress(userId, request);

        return new ResponseEntity<>("Address Addedd Successfully", HttpStatus.CREATED);
    }

    @PatchMapping("/{addressId}")
    public ResponseEntity<Void> makeDefaultAddress(@RequestHeader("X-USER-ID") Long userId, @PathVariable String addressId){
        addressService.makeDefaultAddress(userId, addressId);

        return  ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updatedAddress(@RequestHeader("X-USER-ID") Long userId, @PathVariable String addressId,  @RequestBody AddressRequest updatedRequest){
        return new ResponseEntity<>(addressService.updatedAddress(userId, addressId, updatedRequest), HttpStatus.OK);
    }
}
