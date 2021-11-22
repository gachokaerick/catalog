package com.gachokaerick.eshop.catalog.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.model.CatalogItemMapper;
import com.gachokaerick.eshop.catalog.model.CatalogItemMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class CatalogItemMapperTest {

    private CatalogItemMapper catalogItemMapper;

    @BeforeEach
    public void setUp() {
        catalogItemMapper = new CatalogItemMapperImpl();
    }
}
