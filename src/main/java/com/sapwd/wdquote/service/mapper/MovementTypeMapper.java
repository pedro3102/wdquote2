package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.MovementType;
import com.sapwd.wdquote.service.dto.MovementTypeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link MovementType} and its DTO {@link MovementTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface MovementTypeMapper extends EntityMapper<MovementTypeDTO, MovementType> {
    @Named("movementTypeBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codeName", source = "codeName")
    MovementTypeDTO toDtoBasic(MovementType s);

    //    @Named("movementTypeId")
//    @BeanMapping(ignoreByDefault = true)
//    @Mapping(target = "id", source = "id")
//    @Mapping(target = "code", source = "code")
//    @Mapping(target = "name", source = "name")
//    @Mapping(target = "type", source = "type")
//    @Mapping(target = "counterpart", source = "counterpart")
    @Mapping(target = "oppositeMovementType", source = "oppositeMovementType", qualifiedByName = "movementTypeBasic")
    MovementTypeDTO toDto(MovementType s);
}
