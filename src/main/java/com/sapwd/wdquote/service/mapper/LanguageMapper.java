package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Language;
import com.sapwd.wdquote.service.dto.LanguageDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Language} and its DTO {@link LanguageDTO}.
 */
@Mapper(componentModel = "spring")
public interface LanguageMapper extends EntityMapper<LanguageDTO, Language> {
    @Named("languageId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "code", source = "code")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "id", source = "id")
    LanguageDTO toDtoBasic(Language language);
}
