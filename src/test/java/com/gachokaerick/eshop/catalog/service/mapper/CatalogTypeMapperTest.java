package com.gachokaerick.eshop.catalog.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.service.mapper.CatalogTypeMapper;
import org.junit.jupiter.api.BeforeEach;

class CatalogTypeMapperTest {

    private CatalogTypeMapper catalogTypeMapper;

    @BeforeEach
    public void setUp() {
        catalogTypeMapper = new CatalogTypeMapperImpl();
    }
}
