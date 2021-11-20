package com.gachokaerick.eshop.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.catalog.IntegrationTest;
import com.gachokaerick.eshop.catalog.domain.CatalogType;
import com.gachokaerick.eshop.catalog.repository.CatalogTypeRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogTypeDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogTypeMapper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CatalogTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogTypeResourceIT {

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalog-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogTypeRepository catalogTypeRepository;

    @Autowired
    private CatalogTypeMapper catalogTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogTypeMockMvc;

    private CatalogType catalogType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogType createEntity(EntityManager em) {
        CatalogType catalogType = new CatalogType().type(DEFAULT_TYPE);
        return catalogType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogType createUpdatedEntity(EntityManager em) {
        CatalogType catalogType = new CatalogType().type(UPDATED_TYPE);
        return catalogType;
    }

    @BeforeEach
    public void initTest() {
        catalogType = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogType() throws Exception {
        int databaseSizeBeforeCreate = catalogTypeRepository.findAll().size();
        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);
        restCatalogTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogType testCatalogType = catalogTypeList.get(catalogTypeList.size() - 1);
        assertThat(testCatalogType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void createCatalogTypeWithExistingId() throws Exception {
        // Create the CatalogType with an existing ID
        catalogType.setId(1L);
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        int databaseSizeBeforeCreate = catalogTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogTypeRepository.findAll().size();
        // set the field null
        catalogType.setType(null);

        // Create the CatalogType, which fails.
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        restCatalogTypeMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogTypes() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        // Get all the catalogTypeList
        restCatalogTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogType.getId().intValue())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)));
    }

    @Test
    @Transactional
    void getCatalogType() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        // Get the catalogType
        restCatalogTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogType.getId().intValue()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE));
    }

    @Test
    @Transactional
    void getNonExistingCatalogType() throws Exception {
        // Get the catalogType
        restCatalogTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogType() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();

        // Update the catalogType
        CatalogType updatedCatalogType = catalogTypeRepository.findById(catalogType.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogType are not directly saved in db
        em.detach(updatedCatalogType);
        updatedCatalogType.type(UPDATED_TYPE);
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(updatedCatalogType);

        restCatalogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
        CatalogType testCatalogType = catalogTypeList.get(catalogTypeList.size() - 1);
        assertThat(testCatalogType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogTypeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogTypeWithPatch() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();

        // Update the catalogType using partial update
        CatalogType partialUpdatedCatalogType = new CatalogType();
        partialUpdatedCatalogType.setId(catalogType.getId());

        restCatalogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogType))
            )
            .andExpect(status().isOk());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
        CatalogType testCatalogType = catalogTypeList.get(catalogTypeList.size() - 1);
        assertThat(testCatalogType.getType()).isEqualTo(DEFAULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCatalogTypeWithPatch() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();

        // Update the catalogType using partial update
        CatalogType partialUpdatedCatalogType = new CatalogType();
        partialUpdatedCatalogType.setId(catalogType.getId());

        partialUpdatedCatalogType.type(UPDATED_TYPE);

        restCatalogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogType.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogType))
            )
            .andExpect(status().isOk());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
        CatalogType testCatalogType = catalogTypeList.get(catalogTypeList.size() - 1);
        assertThat(testCatalogType.getType()).isEqualTo(UPDATED_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogTypeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogType() throws Exception {
        int databaseSizeBeforeUpdate = catalogTypeRepository.findAll().size();
        catalogType.setId(count.incrementAndGet());

        // Create the CatalogType
        CatalogTypeDTO catalogTypeDTO = catalogTypeMapper.toDto(catalogType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogTypeMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogTypeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogType in the database
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogType() throws Exception {
        // Initialize the database
        catalogTypeRepository.saveAndFlush(catalogType);

        int databaseSizeBeforeDelete = catalogTypeRepository.findAll().size();

        // Delete the catalogType
        restCatalogTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogType.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogType> catalogTypeList = catalogTypeRepository.findAll();
        assertThat(catalogTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
