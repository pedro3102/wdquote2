package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Systema;
import com.sapwd.wdquote.service.dto.SystemaDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Systema} and its DTO {@link SystemaDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemaMapper extends EntityMapper<SystemaDTO, Systema> {
    @Named("systemBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SystemaDTO toDtoBasic(Systema s);
}
