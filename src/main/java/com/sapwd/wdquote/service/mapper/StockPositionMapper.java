package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.StockPosition;
import com.sapwd.wdquote.service.dto.StockPositionDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link StockPosition} and its DTO {@link StockPositionDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockPositionMapper extends EntityMapper<StockPositionDTO, StockPosition> {
    @Named("stockPositionBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StockPositionDTO toDtoBasic(StockPosition s);
}
