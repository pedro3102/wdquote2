package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.User;
import com.sapwd.wdquote.service.dto.UserDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link User} and its DTO {@link UserDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserMapper extends EntityMapper<UserDTO, User> {}
