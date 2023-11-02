package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.TaxAreaCode;
import com.sapwd.wdquote.service.dto.TaxAreaCodeDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link TaxAreaCode} and its DTO {@link TaxAreaCodeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaxAreaCodeMapper extends EntityMapper<TaxAreaCodeDTO, TaxAreaCode> {
    @Named("taxAreaCodeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    TaxAreaCodeDTO toDtoTaxAreaCodeId(TaxAreaCode taxAreaCode);
}
