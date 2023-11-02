package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Location;
import com.sapwd.wdquote.service.dto.LocationDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Location} and its DTO {@link LocationDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocationMapper extends EntityMapper<LocationDTO, Location> {
    @Named("locationBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codeName", source = "codeName")
    LocationDTO toDtoBasic(Location location);
}
