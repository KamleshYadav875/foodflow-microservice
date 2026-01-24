package com.foodflow.customer_service.repository;

import com.foodflow.customer_service.entity.Address;
import com.foodflow.customer_service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByCustomer(Customer myProfile);

    Address findByCustomerAndIsDefaultTrue(Customer customer);

    Optional<Address> findByCustomerAndId(Customer customer, Long addressId);
}
