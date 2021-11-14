package com.gachokaerick.eshop.catalog.repository;

import com.gachokaerick.eshop.catalog.domain.CatalogBrand;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the CatalogBrand entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CatalogBrandRepository extends JpaRepository<CatalogBrand, Long> {}
