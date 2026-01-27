package com.foodflow.customer_service.controller;

import com.foodflow.customer_service.dto.AddressRequest;
import com.foodflow.customer_service.dto.AddressResponse;
import com.foodflow.customer_service.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @PostMapping
    public ResponseEntity<AddressResponse> addAddress(@RequestHeader("X-USER-ID") Long userId, @RequestBody AddressRequest request){

        return new ResponseEntity<>(addressService.addAddress(userId, request), HttpStatus.CREATED);
    }

    @PatchMapping("/{addressId}/default")
    public ResponseEntity<Void> makeDefaultAddress(@RequestHeader("X-USER-ID") Long userId, @PathVariable String addressId){
        addressService.makeDefaultAddress(userId, addressId);

        return  ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponse> updatedAddress(@RequestHeader("X-USER-ID") Long userId, @PathVariable String addressId,  @RequestBody AddressRequest updatedRequest){
        return new ResponseEntity<>(addressService.updatedAddress(userId, addressId, updatedRequest), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<AddressResponse>> getAddress(@RequestHeader("X-USER-ID") Long userId){
        return new ResponseEntity<>(addressService.getAddress(userId), HttpStatus.OK);
    }
}
