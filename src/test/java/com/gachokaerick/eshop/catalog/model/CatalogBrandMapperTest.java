package com.gachokaerick.eshop.catalog.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;

class CatalogBrandMapperTest {

    private CatalogBrandMapper catalogBrandMapper;

    @BeforeEach
    public void setUp() {
        catalogBrandMapper = new CatalogBrandMapperImpl();
    }
}
