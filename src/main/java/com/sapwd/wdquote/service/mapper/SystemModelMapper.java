package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.SystemModel;
import com.sapwd.wdquote.service.dto.SystemModelDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link SystemModel} and its DTO {@link SystemModelDTO}.
 */
@Mapper(componentModel = "spring", uses = SystemaMapper.class)
public interface SystemModelMapper extends EntityMapper<SystemModelDTO, SystemModel> {
    @Mapping(target = "system", source = "system", qualifiedByName = "systemBasic")
    SystemModelDTO toDto(SystemModel s);

    @Named("systemModelBasicId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    SystemModelDTO toDtoSystemModelBasic(SystemModel systemModel);

    @Named("systemModelIdSet")
    default Set<SystemModelDTO> toDtoSystemModelIdSet(Set<SystemModel> systemModel) {
        return systemModel.stream().map(this::toDtoSystemModelBasic).collect(Collectors.toSet());
    }
}
