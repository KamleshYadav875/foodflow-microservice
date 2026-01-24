package com.foodflow.customer_service.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_address_customer_label",
                        columnNames = { "customer_id", "label" }
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private String address;

    private String city;

    private String state;

    private String zipcode;

    private Boolean isDefault = false;

    private String label;

    private Double latitude;
    private Double longitude;

}
