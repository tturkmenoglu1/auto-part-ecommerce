package com.ape.controller;

import com.ape.dto.UserAddressDTO;
import com.ape.dto.request.UserAddressRequest;
import com.ape.dto.request.UserAddressUpdate;
import com.ape.service.UserAddressService;
import com.ape.utility.APEResponse;
import com.ape.utility.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user-address")
public class UserAddressController {

    private final UserAddressService userAddressService;

    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> saveUserAddress(@Valid @RequestBody UserAddressRequest userAddressRequest) {
        UserAddressDTO userAddressDTO= userAddressService.saveAddress(userAddressRequest);
        APEResponse response = new APEResponse(ResponseMessage.USER_ADDRESS_CREATED_RESPONSE_MESSAGE, true,userAddressDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<List<UserAddressDTO>> getAllAddress(){
        List<UserAddressDTO> userAddressDTOS=userAddressService.getAllAddresses();
        return ResponseEntity.ok(userAddressDTOS);
    }

    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserAddressDTO> getAddressById(@PathVariable("id") Long id){
        UserAddressDTO userAddressDTO=userAddressService.getAddressesById(id);
        return ResponseEntity.ok(userAddressDTO);
    }

    @PutMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<UserAddressDTO> authUpdateAddress(@PathVariable("id") Long id, @Valid @RequestBody UserAddressUpdate userAddressUpdate){
        UserAddressDTO userAddressDTO=userAddressService.authUpdateAddress(id,userAddressUpdate);
        return ResponseEntity.ok(userAddressDTO);
    }

    @DeleteMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<APEResponse> deleteAuthUserAddress(@PathVariable("id") Long id){
        userAddressService.removeUserAddressById(id);
        APEResponse response = new APEResponse(ResponseMessage.USER_ADDRESS_DELETE_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }

}
