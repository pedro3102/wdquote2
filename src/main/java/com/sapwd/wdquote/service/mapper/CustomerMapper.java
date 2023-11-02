package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Customer;
import com.sapwd.wdquote.service.dto.CustomerDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {DeliveryZoneMapper.class, TaxAreaCodeMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "deliveryZone", source = "deliveryZone", qualifiedByName = "deliveryZoneId")
    @Mapping(target = "taxAreaCode", source = "taxAreaCode", qualifiedByName = "taxAreaCodeId")
    CustomerDTO toDto(Customer s);

    @Named("customerBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codeName", source = "codeName")
    CustomerDTO toDtoBasic(Customer s);
}
