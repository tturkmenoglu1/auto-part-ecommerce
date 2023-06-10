package com.ape.repository;

import com.ape.model.Order;
import com.ape.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    boolean existsByAddress(UserAddress userAddress);
}
