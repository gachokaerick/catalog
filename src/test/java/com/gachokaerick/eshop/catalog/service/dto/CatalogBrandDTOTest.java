package com.gachokaerick.eshop.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.service.dto.CatalogBrandDTO;
import com.gachokaerick.eshop.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogBrandDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogBrandDTO.class);
        CatalogBrandDTO catalogBrandDTO1 = new CatalogBrandDTO();
        catalogBrandDTO1.setId(1L);
        CatalogBrandDTO catalogBrandDTO2 = new CatalogBrandDTO();
        assertThat(catalogBrandDTO1).isNotEqualTo(catalogBrandDTO2);
        catalogBrandDTO2.setId(catalogBrandDTO1.getId());
        assertThat(catalogBrandDTO1).isEqualTo(catalogBrandDTO2);
        catalogBrandDTO2.setId(2L);
        assertThat(catalogBrandDTO1).isNotEqualTo(catalogBrandDTO2);
        catalogBrandDTO1.setId(null);
        assertThat(catalogBrandDTO1).isNotEqualTo(catalogBrandDTO2);
    }
}
