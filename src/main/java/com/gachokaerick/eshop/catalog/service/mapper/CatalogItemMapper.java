package com.gachokaerick.eshop.catalog.service.mapper;

import com.gachokaerick.eshop.catalog.domain.CatalogItem;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
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
