package com.sapwd.wdquote.service.mapper;

import com.sapwd.wdquote.domain.Product;
import com.sapwd.wdquote.service.dto.ProductDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {UnitOfMeasureMapper.class, ProductCategoryMapper.class, SystemModelMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "uom", source = "uom", qualifiedByName = "unitOfMeasureBasic")
    @Mapping(target = "uomWeight", source = "uomWeight", qualifiedByName = "unitOfMeasureBasic")
    @Mapping(target = "category", source = "category", qualifiedByName = "productCategoryBasic")
    @Mapping(target = "systemModels", source = "systemModels", qualifiedByName = "systemModelIdSet")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeSystemModel", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("productBasic")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "code", source = "code")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "uom", source = "uom", qualifiedByName = "unitOfMeasureBasic")
    ProductDTO toDtoBasic(Product product);
}
