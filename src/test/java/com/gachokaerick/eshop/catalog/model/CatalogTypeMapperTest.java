package com.gachokaerick.eshop.catalog.model;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.model.CatalogTypeMapper;
import com.gachokaerick.eshop.catalog.model.CatalogTypeMapperImpl;
import org.junit.jupiter.api.BeforeEach;

class CatalogTypeMapperTest {

    private CatalogTypeMapper catalogTypeMapper;

    @BeforeEach
    public void setUp() {
        catalogTypeMapper = new CatalogTypeMapperImpl();
    }
}
