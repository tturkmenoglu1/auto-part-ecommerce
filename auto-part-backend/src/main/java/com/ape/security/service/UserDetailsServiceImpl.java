package com.ape.security.service;

import com.ape.exception.ResourceNotFoundException;
import com.ape.utility.ErrorMessage;
import com.ape.model.User;
import com.ape.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String  email) throws UsernameNotFoundException {

        User user =  userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
        return UserDetailsImpl.build(user);
    }
}
