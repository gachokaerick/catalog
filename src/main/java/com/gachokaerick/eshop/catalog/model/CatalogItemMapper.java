package com.gachokaerick.eshop.catalog.model;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogItem} and its DTO {@link CatalogItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogBrandMapper.class, CatalogTypeMapper.class })
public interface CatalogItemMapper extends EntityMapper<CatalogItemDTO, CatalogItem> {
    @Mapping(target = "catalogBrand", source = "catalogBrand", qualifiedByName = "brand")
    @Mapping(target = "catalogType", source = "catalogType", qualifiedByName = "type")
    CatalogItemDTO toDto(CatalogItem s);
}
