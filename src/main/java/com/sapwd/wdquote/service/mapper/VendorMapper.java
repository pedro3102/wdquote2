package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Vendor;
import com.sapwd.wdquote.service.dto.VendorDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Vendor} and its DTO {@link VendorDTO}.
 */
@Mapper(componentModel = "spring")
public interface VendorMapper extends EntityMapper<VendorDTO, Vendor> {
    @Named("vendorBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "codeName", source = "codeName")
    VendorDTO toDtoBasic(Vendor vendor);
}
