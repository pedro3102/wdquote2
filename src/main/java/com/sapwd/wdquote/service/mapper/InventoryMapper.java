package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Inventory;
import com.sapwd.wdquote.service.dto.InventoryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Inventory} and its DTO {@link InventoryDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class, LocationMapper.class})
public interface InventoryMapper extends EntityMapper<InventoryDTO, Inventory> {
    @Mapping(target = "location", source = "location", qualifiedByName = "locationBasic")
    @Mapping(target = "product", source = "product", qualifiedByName = "productBasic")
    @Mapping(target = "stockStatus", source = "stockStatus")
    InventoryDTO toDto(Inventory s);
}
