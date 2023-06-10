package com.ape.service;

import com.ape.dto.UserAddressDTO;
import com.ape.dto.request.UserAddressRequest;
import com.ape.dto.request.UserAddressUpdate;
import com.ape.exception.BadRequestException;
import com.ape.exception.ResourceNotFoundException;
import com.ape.mapper.UserAddressMapper;
import com.ape.model.User;
import com.ape.model.UserAddress;
import com.ape.repository.UserAddressRepository;
import com.ape.utility.ErrorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAddressService {

    private final UserAddressRepository userAddressRepository;

    private final UserService userService;

    private final UserAddressMapper userAddressMapper;

    private final OrderService orderService;

    public UserAddressDTO saveAddress(UserAddressRequest userAddressRequest) {
        User user=userService.getCurrentUser();
        UserAddress userAddress= userAddressMapper.userAddressRequestToUserAddress(userAddressRequest);
        userAddress.setUser(user);
        userAddressRepository.save(userAddress);
        return userAddressMapper.userAddressToUserAddressDTO(userAddress);
    }

    public List<UserAddressDTO> getAllAddresses() {
        User user=userService.getCurrentUser();
        List<UserAddress> userAddresses=userAddressRepository.findAllByUser(user);
        return userAddressMapper.map(userAddresses);
    }

    public UserAddressDTO getAddressesById(Long id) {
        UserAddress userAddress=userAddressRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
        return userAddressMapper.userAddressToUserAddressDTO(userAddress);
    }

    public UserAddressDTO authUpdateAddress(Long id, UserAddressUpdate userAddressUpdate) {
        User user=userService.getCurrentUser();
        UserAddress userAddress=getAddressById(id);

        userAddress.setUser(user);
        userAddress.setTitle(userAddressUpdate.getTitle());
        userAddress.setFirstName(userAddressUpdate.getFirstName());
        userAddress.setLastName(userAddressUpdate.getLastName());
        userAddress.setEmail(userAddressUpdate.getEmail());
        userAddress.setPhone(userAddressUpdate.getPhone());
        userAddress.setProvince(userAddressUpdate.getProvince());
        userAddress.setAddress(userAddressUpdate.getAddress());
        userAddress.setCity(userAddressUpdate.getCity());
        userAddress.setCountry(userAddressUpdate.getCountry());
        userAddress.setUpdateAt(userAddressUpdate.getUpdateAt());

        userAddressRepository.save(userAddress);
        return userAddressMapper.userAddressToUserAddressDTO(userAddress);
    }

    public UserAddress getAddressById(Long id) {
        return userAddressRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.RESOURCE_NOT_FOUND_MESSAGE,id)));
    }

    public void removeUserAddressById(Long id) {
        User user=userService.getCurrentUser();
        UserAddress userAddress=getAddressById(id);
        boolean existAddress= orderService.existsByAddress(userAddress);
        if (existAddress) {
            throw new BadRequestException(String.format(ErrorMessage.USER_ADDRESS_USED_MESSAGE));
        }
        userAddressRepository.delete(userAddress);
    }
}
