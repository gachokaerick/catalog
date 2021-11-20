package com.gachokaerick.eshop.catalog.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogBrandMapperTest {

    private CatalogBrandMapper catalogBrandMapper;

    @BeforeEach
    public void setUp() {
        catalogBrandMapper = new CatalogBrandMapperImpl();
    }
}
