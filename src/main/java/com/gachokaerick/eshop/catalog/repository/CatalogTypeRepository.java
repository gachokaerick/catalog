package com.gachokaerick.eshop.catalog.repository;

import com.gachokaerick.eshop.catalog.model.CatalogType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogTypeRepository extends JpaRepository<CatalogType, Long> {}
