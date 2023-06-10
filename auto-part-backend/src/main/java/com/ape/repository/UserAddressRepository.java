package com.ape.repository;

import com.ape.model.User;
import com.ape.model.UserAddress;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserAddressRepository extends JpaRepository<UserAddress, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<UserAddress> findAllByUser(User user);
}
