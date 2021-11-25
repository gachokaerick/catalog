package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapper;
import org.junit.jupiter.api.BeforeEach;

class CatalogItemMapperTest {

    private CatalogItemMapper catalogItemMapper;

    @BeforeEach
    public void setUp() {
        catalogItemMapper = new CatalogItemMapperImpl();
    }
}
