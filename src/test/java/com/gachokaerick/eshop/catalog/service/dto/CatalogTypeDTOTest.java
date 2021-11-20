package com.gachokaerick.eshop.catalog.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.gachokaerick.eshop.catalog.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CatalogTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CatalogTypeDTO.class);
        CatalogTypeDTO catalogTypeDTO1 = new CatalogTypeDTO();
        catalogTypeDTO1.setId(1L);
        CatalogTypeDTO catalogTypeDTO2 = new CatalogTypeDTO();
        assertThat(catalogTypeDTO1).isNotEqualTo(catalogTypeDTO2);
        catalogTypeDTO2.setId(catalogTypeDTO1.getId());
        assertThat(catalogTypeDTO1).isEqualTo(catalogTypeDTO2);
        catalogTypeDTO2.setId(2L);
        assertThat(catalogTypeDTO1).isNotEqualTo(catalogTypeDTO2);
        catalogTypeDTO1.setId(null);
        assertThat(catalogTypeDTO1).isNotEqualTo(catalogTypeDTO2);
    }
}
