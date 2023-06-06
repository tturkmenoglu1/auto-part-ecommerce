package com.ape.service;

import com.ape.dto.request.RegisterRequest;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.model.ConfirmationToken;
import com.ape.model.Role;
import com.ape.model.User;
import com.ape.model.enums.RoleType;
import com.ape.model.enums.UserStatus;
import com.ape.repository.UserRepository;
import com.ape.service.email.EmailSender;
import com.ape.service.email.EmailService;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final EmailSender emailSender;

    private final EmailService emailService;

    @Value("${autopart.app.backendLink}")
    private String backendLink;

    public String saveUser(RegisterRequest registerRequest) {
        Role role = roleService.findByRoleName(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = null;
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            user = getUserByEmail(registerRequest.getEmail());
            if(user.getStatus()!= UserStatus.ANONYMOUS) {
                throw new ConflictException(String.format(ErrorMessage.EMAIL_EXIST_MESSAGE,
                        registerRequest.getEmail()));
            }else{
                user.setFirstName(registerRequest.getFirstName());
                user.setLastName(registerRequest.getLastName());
                user.setEmail(registerRequest.getEmail());
                user.setBirthDate(registerRequest.getBirthDate());
                user.setPhone(registerRequest.getPhone());
                user.setStatus(UserStatus.PENDING);
                user.setPassword(encodedPassword);
                userRepository.save(user);
            }
        }else {
            user = new User();
            user.setFirstName(registerRequest.getFirstName());
            user.setLastName(registerRequest.getLastName());
            user.setEmail(registerRequest.getEmail());
            user.setBirthDate(registerRequest.getBirthDate());
            user.setPassword(encodedPassword);
            user.setPhone(registerRequest.getPhone());
            user.setRoles(roles);
            user.setStatus(UserStatus.PENDING);
            userRepository.save(user);
        }


        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(token, LocalDateTime.now(),LocalDateTime.now().plusDays(1),user);
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        String link = backendLink+"confirm?token="+token;
        emailSender.send(
                registerRequest.getEmail(),
                emailService.buildRegisterEmail(registerRequest.getFirstName(),link));
        return token;
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
    }
}
