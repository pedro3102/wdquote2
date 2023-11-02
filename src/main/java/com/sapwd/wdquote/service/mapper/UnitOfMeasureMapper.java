package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.UnitOfMeasure;
import com.sapwd.wdquote.service.dto.UnitOfMeasureDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link UnitOfMeasure} and its DTO {@link UnitOfMeasureDTO}.
 */
@Mapper(componentModel = "spring")
public interface UnitOfMeasureMapper extends EntityMapper<UnitOfMeasureDTO, UnitOfMeasure> {
    @Named("unitOfMeasureBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "abbreviation", source = "abbreviation")
    @Mapping(target = "allowsDecimal", source = "allowsDecimal")
    UnitOfMeasureDTO toDtoUnitOfMeasureBasic(UnitOfMeasure unitOfMeasure);
}
