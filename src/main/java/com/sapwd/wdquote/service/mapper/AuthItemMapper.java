package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.AuthItem;
import com.sapwd.wdquote.service.dto.AuthItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity {@link AuthItem} and its DTO {@link AuthItemDTO}.
 */
@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AuthItemMapper extends EntityMapper<AuthItemDTO, AuthItem> {}
