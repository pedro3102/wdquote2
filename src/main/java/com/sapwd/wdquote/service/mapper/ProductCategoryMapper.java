package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.ProductCategory;
import com.sapwd.wdquote.service.dto.ProductCategoryDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link ProductCategory} and its DTO {@link ProductCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductCategoryMapper extends EntityMapper<ProductCategoryDTO, ProductCategory> {
    @Named("productCategoryBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductCategoryDTO toDtoProductCategoryBasic(ProductCategory productCategory);
}
