package com.gachokaerick.eshop.catalog.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CatalogItemMapperTest {

    private CatalogItemMapper catalogItemMapper;

    @BeforeEach
    public void setUp() {
        catalogItemMapper = new CatalogItemMapperImpl();
    }
}
