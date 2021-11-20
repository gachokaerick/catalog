package com.gachokaerick.eshop.catalog.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogTypeMapperTest {

    private CatalogTypeMapper catalogTypeMapper;

    @BeforeEach
    public void setUp() {
        catalogTypeMapper = new CatalogTypeMapperImpl();
    }
}
