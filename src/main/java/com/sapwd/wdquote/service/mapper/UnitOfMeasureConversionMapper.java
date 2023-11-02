package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.UnitOfMeasureConversion;
import com.sapwd.wdquote.service.dto.UnitOfMeasureConversionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link UnitOfMeasureConversion} and its DTO {@link UnitOfMeasureConversionDTO}.
 */
@Mapper(componentModel = "spring", uses = UnitOfMeasureMapper.class)
public interface UnitOfMeasureConversionMapper extends EntityMapper<UnitOfMeasureConversionDTO, UnitOfMeasureConversion> {
    @Mapping(target = "uom", source = "uom", qualifiedByName = "unitOfMeasureBasic")
    @Mapping(target = "uomEquivalent", source = "uomEquivalent", qualifiedByName = "unitOfMeasureBasic")
    UnitOfMeasureConversionDTO toDto(UnitOfMeasureConversion s);
}
