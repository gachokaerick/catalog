package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogItem entity.
 */
@SuppressWarnings("unused")
@Repository
interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {}
