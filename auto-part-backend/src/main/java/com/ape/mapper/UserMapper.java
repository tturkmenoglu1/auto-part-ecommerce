package com.ape.mapper;

import com.ape.dto.UserDTO;
import com.ape.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);
}
