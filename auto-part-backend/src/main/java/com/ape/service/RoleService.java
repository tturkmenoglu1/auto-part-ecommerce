package com.ape.service;

import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import com.ape.model.Role;
import com.ape.model.enums.RoleType;
import com.ape.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByRoleName(RoleType roleType) {
        return roleRepository.findByRoleName(roleType).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.ROLE_NOT_FOUND_MESSAGE, roleType.name())));
    }
}
