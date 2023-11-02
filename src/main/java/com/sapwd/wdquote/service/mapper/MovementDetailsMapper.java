package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.MovementDetails;
import com.sapwd.wdquote.service.dto.MovementDetailsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link MovementDetails} and its DTO {@link MovementDetailsDTO}.
 */
@Mapper(componentModel = "spring", uses = {MovementMapper.class, ProductMapper.class, StockPositionMapper.class})
public interface MovementDetailsMapper extends EntityMapper<MovementDetailsDTO, MovementDetails> {
    @Mapping(target = "movement", source = "movement", qualifiedByName = "movementId")
    @Mapping(target = "product", source = "product", qualifiedByName = "productBasic")
    @Mapping(target = "stockPosition", source = "stockPosition", qualifiedByName = "stockPositionBasic")
    MovementDetailsDTO toDto(MovementDetails s);
}
