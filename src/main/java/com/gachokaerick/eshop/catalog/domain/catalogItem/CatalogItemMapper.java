package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapper;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogTypeMapper;
import com.gachokaerick.eshop.catalog.service.mapper.EntityMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link CatalogItem} and its DTO {@link CatalogItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { CatalogBrandMapper.class, CatalogTypeMapper.class })
public interface CatalogItemMapper extends EntityMapper<CatalogItemDTO, CatalogItem> {
    @Mapping(target = "catalogBrand", source = "catalogBrand", qualifiedByName = "brand")
    @Mapping(target = "catalogType", source = "catalogType", qualifiedByName = "type")
    CatalogItemDTO toDto(CatalogItem s);
}
