package com.gachokaerick.eshop.catalog.service.mapper;

import com.gachokaerick.eshop.catalog.model.CatalogType;
import com.gachokaerick.eshop.catalog.service.dto.CatalogTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogType} and its DTO {@link CatalogTypeDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogTypeMapper extends EntityMapper<CatalogTypeDTO, CatalogType> {
    @Named("type")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "type", source = "type")
    CatalogTypeDTO toDtoType(CatalogType catalogType);
}
