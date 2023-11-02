package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.DeliveryZone;
import com.sapwd.wdquote.service.dto.DeliveryZoneDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link DeliveryZone} and its DTO {@link DeliveryZoneDTO}.
 */
@Mapper(componentModel = "spring")
public interface DeliveryZoneMapper extends EntityMapper<DeliveryZoneDTO, DeliveryZone> {
    @Named("deliveryZoneId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    DeliveryZoneDTO toDtoDeliveryZoneId(DeliveryZone deliveryZone);
}
