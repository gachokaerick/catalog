package com.gachokaerick.eshop.catalog.service.mapper;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapper;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class CatalogItemMapperTest {

    private CatalogItemMapper catalogItemMapper;

    @BeforeEach
    public void setUp() {
        catalogItemMapper = new CatalogItemMapperImpl();
    }
}
