package com.ape.utility;

import com.ape.model.Role;
import com.ape.model.enums.RoleType;
import com.ape.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {


        roleRepository.save(new Role( 1,RoleType.ROLE_USER));
        roleRepository.save(new Role(2,RoleType.ROLE_ADMIN));
    }
}
