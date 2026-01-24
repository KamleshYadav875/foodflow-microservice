package com.foodflow.restaurant_service.controller;

import com.foodflow.restaurant_service.dto.OfferRequest;
import com.foodflow.restaurant_service.dto.OfferResponse;
import com.foodflow.restaurant_service.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/restaurant/offer")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @PostMapping
    public ResponseEntity<OfferResponse> createOffer(@RequestHeader("X-USER-ID") Long userId,  @RequestBody OfferRequest request){
        return new ResponseEntity<OfferResponse>(offerService.createOffer(userId, request), HttpStatus.CREATED);
    }

    @PutMapping("/{offerId}")
    public ResponseEntity<OfferResponse> updateOffer(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long offerId,  @RequestBody OfferRequest request){
        return new ResponseEntity<OfferResponse>(offerService.updateOffer(userId, offerId, request), HttpStatus.OK);
    }

    @DeleteMapping("/{offerId}")
    public ResponseEntity<Void> deleteOffer(@RequestHeader("X-USER-ID") Long userId, @PathVariable Long offerId){
        offerService.deleteOffer(userId, offerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<OfferResponse>> getOffer(@RequestHeader("X-USER-ID") Long userId){
        return new ResponseEntity<List<OfferResponse>>(offerService.getOffer(userId), HttpStatus.OK);
    }
}
