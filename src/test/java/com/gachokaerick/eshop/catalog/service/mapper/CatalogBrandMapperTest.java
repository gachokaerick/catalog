package com.gachokaerick.eshop.catalog.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapper;
import org.junit.jupiter.api.BeforeEach;

class CatalogBrandMapperTest {

    private CatalogBrandMapper catalogBrandMapper;

    @BeforeEach
    public void setUp() {
        catalogBrandMapper = new CatalogBrandMapperImpl();
    }
}
