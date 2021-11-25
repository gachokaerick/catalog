package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CatalogItemDomainRepository {
    CatalogItemDTO create(CatalogItemDomain catalogItemDomain);

    CatalogItemDTO update(CatalogItemDomain catalogItemDomain);

    Optional<CatalogItemDTO> partialUpdate(CatalogItemDomain catalogItemDomain);

    Page<CatalogItemDTO> findAll(Pageable pageable);

    Optional<CatalogItemDTO> findOne(Long id);

    void delete(Long id);
}
