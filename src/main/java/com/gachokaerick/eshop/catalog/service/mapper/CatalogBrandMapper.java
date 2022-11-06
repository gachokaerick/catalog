package com.gachokaerick.eshop.catalog.service.mapper;

import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.service.dto.CatalogBrandDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CatalogBrand} and its DTO {@link CatalogBrandDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CatalogBrandMapper extends EntityMapper<CatalogBrandDTO, CatalogBrand> {
    @Named("brand")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "brand", source = "brand")
    CatalogBrandDTO toDtoBrand(CatalogBrand catalogBrand);
}
