package com.ape.repository;

import com.ape.model.Role;
import com.ape.model.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {

    Optional<Role> findByRoleName(RoleType roleType);
}
