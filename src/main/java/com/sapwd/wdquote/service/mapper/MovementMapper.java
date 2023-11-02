package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Movement;
import com.sapwd.wdquote.service.dto.MovementDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Movement} and its DTO {@link MovementDTO}.
 */
@Mapper(componentModel = "spring", uses = { MovementTypeMapper.class, LocationMapper.class, VendorMapper.class, CustomerMapper.class })
public interface MovementMapper extends EntityMapper<MovementDTO, Movement> {
    //    @Mapping(target = "movementType", source = "movementType", qualifiedByName = "movementTypeId")
    @Mapping(target = "location", source = "location", qualifiedByName = "locationBasic")
    @Mapping(target = "counterpartLocation", source = "counterpartLocation", qualifiedByName = "locationBasic")
    @Mapping(target = "counterpartVendor", source = "counterpartVendor", qualifiedByName = "vendorBasic")
    @Mapping(target = "counterpartCustomer", source = "counterpartCustomer", qualifiedByName = "customerBasic")
    MovementDTO toDto(Movement s);

    @Named("movementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "no", source = "no")
    @Mapping(target = "reference", source = "reference")
    @Mapping(target = "date", source = "date")
    @Mapping(target = "note", source = "note")
    @Mapping(target = "canceledDate", source = "canceledDate")
    @Mapping(target = "consecutive", source = "consecutive")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "counterpart", source = "counterpart")
    @Mapping(target = "movementType", source = "movementType", qualifiedByName = "movementTypeBasic")
    @Mapping(target = "location", source = "location")
    MovementDTO toDtoMovementId(Movement movement);
}
