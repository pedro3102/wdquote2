package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.CustomerContact;
import com.sapwd.wdquote.service.dto.CustomerContactDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link CustomerContact} and its DTO {@link CustomerContactDTO}.
 */
@Mapper(componentModel = "spring", uses = { CustomerMapper.class, LanguageMapper.class })
public interface CustomerContactMapper extends EntityMapper<CustomerContactDTO, CustomerContact> {
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerBasic")
    @Mapping(target = "language", source = "language", qualifiedByName = "languageId")
    CustomerContactDTO toDto(CustomerContact s);
}
