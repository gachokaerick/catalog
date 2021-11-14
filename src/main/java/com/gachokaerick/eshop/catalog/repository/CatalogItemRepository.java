package com.gachokaerick.eshop.catalog.repository;

import com.gachokaerick.eshop.catalog.domain.CatalogItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogItemRepository extends JpaRepository<CatalogItem, Long> {}
