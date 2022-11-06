package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.service.mapper.*;
import java.util.Objects;
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

    default void partialUpdate(CatalogItem entity, CatalogItemDTO dto) {
        CatalogBrandMapper catalogBrandMapper = new CatalogBrandMapperImpl();
        CatalogTypeMapper catalogTypeMapper = new CatalogTypeMapperImpl();

        if (dto == null) {
            return;
        }

        if (dto.getId() != null && entity.getId() != null) {
            if (!Objects.equals(dto.getId(), entity.getId())) {
                return;
            }
        }

        if (dto.getId() != null) {
            entity.setId(dto.getId());
        }
        if (dto.getName() != null) {
            entity.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            entity.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {
            entity.setPrice(dto.getPrice());
        }
        if (dto.getPictureFileName() != null) {
            entity.setPictureFileName(dto.getPictureFileName());
        }
        if (dto.getPictureUrl() != null) {
            entity.setPictureUrl(dto.getPictureUrl());
        }
        if (dto.getAvailableStock() != null) {
            entity.setAvailableStock(dto.getAvailableStock());
        }
        if (dto.getRestockThreshold() != null) {
            entity.setRestockThreshold(dto.getRestockThreshold());
        }
        if (dto.getMaxStockThreshold() != null) {
            entity.setMaxStockThreshold(dto.getMaxStockThreshold());
        }
        if (dto.getOnReorder() != null) {
            entity.setOnReorder(dto.getOnReorder());
        }
        if (dto.getCatalogBrand() != null) {
            entity.setCatalogBrand(catalogBrandMapper.toEntity(dto.getCatalogBrand()));
        }
        if (dto.getCatalogType() != null) {
            entity.setCatalogType(catalogTypeMapper.toEntity(dto.getCatalogType()));
        }
    }
}
