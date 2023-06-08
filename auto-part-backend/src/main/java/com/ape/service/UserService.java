package com.ape.service;

import com.ape.dto.UserDTO;
import com.ape.dto.request.LoginRequest;
import com.ape.dto.request.RegisterRequest;
import com.ape.exception.BadRequestException;
import com.ape.exception.ConflictException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.UserMapper;
import com.ape.model.*;
import com.ape.model.enums.RoleType;
import com.ape.model.enums.UserStatus;
import com.ape.repository.BasketItemRepository;
import com.ape.repository.BasketRepository;
import com.ape.repository.UserRepository;
import com.ape.security.jwt.JwtUtils;
import com.ape.service.email.EmailSender;
import com.ape.service.email.EmailService;
import com.ape.utility.ErrorMessage;
import com.ape.utility.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final BasketRepository basketRepository;

    private final BasketItemRepository basketItemRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;



    @Value("${autopart.app.backendLink}")
    private String backendLink;

    public void saveUser(RegisterRequest registerRequest) {
        Role role = roleService.findByRoleName(RoleType.ROLE_USER);
        Set<Role> roles = new HashSet<>();
        roles.add(role);

        User user = null;
        String encodedPassword = passwordEncoder.encode(registerRequest.getPassword());
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            user = getUserByEmail(registerRequest.getEmail());
            if(user.getStatus()!= UserStatus.ANONYMOUS) {
                throw new ConflictException(String.format(ErrorMessage.EMAIL_ALREADY_EXIST_MESSAGE,
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
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.USER_NOT_FOUND_MESSAGE));
    }

    public void confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService.getToken(token).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,token)));
        if (confirmationToken.getConfirmedAt() != null){
            throw new IllegalStateException(ErrorMessage.EMAIL_ALREADY_CONFIRMED_MESSAGE);
        }
        LocalDateTime expireDate = confirmationToken.getExpiresAt();
        if (expireDate.isBefore(LocalDateTime.now())){
            throw new IllegalStateException(ErrorMessage.TOKEN_EXPIRED_MESSAGE);
        }
        confirmationTokenService.setConfirmedAt(token);
        User user = getUserByEmail(confirmationToken.getUser().getEmail());
        Basket basket = new Basket();
        basket.setBasketUUID(UUID.randomUUID().toString());
        basketRepository.save(basket);
        user.setBasket(basket);
        user.setStatus(UserStatus.ACTIVATED);
        userRepository.save(user);
    }


    public LoginResponse loginUser(String basketUUID,LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.
                authenticate(usernamePasswordAuthenticationToken);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal() ;
        User user = getUserByEmail(userDetails.getUsername());
        Basket anonymousBasket = basketRepository.findByBasketUUID(basketUUID).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,basketUUID)));
        Basket userBasket = user.getBasket();
        if (anonymousBasket.getBasketItem().size()!=0){
            if (userBasket.getBasketItem().size()==0) {
                for (BasketItem anonymousBasketItem: anonymousBasket.getBasketItem()) {
                    BasketItem  basketItem = new BasketItem();
                    basketItem.setProduct(anonymousBasketItem.getProduct());
                    basketItem.setQuantity(anonymousBasketItem.getQuantity());
                    basketItem.setTotalPrice(anonymousBasketItem.getTotalPrice());
                    basketItem.setBasket(userBasket);
                    userBasket.setGrandTotal(anonymousBasket.getGrandTotal());
                    basketItemRepository.save(basketItem);
                }
                basketRepository.save(userBasket);
            }else{
                for (BasketItem anonymousBasketItem: anonymousBasket.getBasketItem()) {
                    boolean merged = false;
                    for (BasketItem userBasketItem: userBasket.getBasketItem()) {
                        if (anonymousBasketItem.getProduct().getId().longValue()==userBasketItem.getProduct().getId().longValue()) {
                            userBasketItem.setQuantity(userBasketItem.getQuantity() + anonymousBasketItem.getQuantity());
                            userBasketItem.setTotalPrice(userBasketItem.getTotalPrice() + anonymousBasketItem.getTotalPrice());
                            userBasket.setGrandTotal(userBasket.getGrandTotal()+anonymousBasketItem.getTotalPrice());
                            merged = true;
                            break;
                        }
                    }
                    if (!merged){
                        BasketItem unmergedItem = new BasketItem();
                        unmergedItem.setQuantity(anonymousBasketItem.getQuantity());
                        unmergedItem.setProduct(anonymousBasketItem.getProduct());
                        unmergedItem.setTotalPrice(anonymousBasketItem.getTotalPrice());
                        unmergedItem.setBasket(userBasket);
                        userBasket.setGrandTotal(userBasket.getGrandTotal()+unmergedItem.getTotalPrice());
                        basketItemRepository.save(unmergedItem);
                    }
                }
            }
        }
        basketRepository.save(userBasket);
        basketRepository.delete(anonymousBasket);


        if (user.getStatus().equals(UserStatus.PENDING)){
            throw new BadRequestException(String.format(ErrorMessage.EMAIL_NOT_CONFIRMED_MESSAGE,user.getEmail()));
        }
        String jwtToken = jwtUtils.generateJwtToken(userDetails);
        String userBasketUUID = user.getBasket().getBasketUUID();
        return new LoginResponse(jwtToken,userBasketUUID);
    }
}
