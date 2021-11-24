package com.gachokaerick.eshop.catalog.domain.catalogItem;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogItemDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogItemDTO.class);
        CatalogItemDTO catalogItemDTO1 = new CatalogItemDTO();
        catalogItemDTO1.setId(1L);
        CatalogItemDTO catalogItemDTO2 = new CatalogItemDTO();
        assertThat(catalogItemDTO1).isNotEqualTo(catalogItemDTO2);
        catalogItemDTO2.setId(catalogItemDTO1.getId());
        assertThat(catalogItemDTO1).isEqualTo(catalogItemDTO2);
        catalogItemDTO2.setId(2L);
        assertThat(catalogItemDTO1).isNotEqualTo(catalogItemDTO2);
        catalogItemDTO1.setId(null);
        assertThat(catalogItemDTO1).isNotEqualTo(catalogItemDTO2);
    }
}
