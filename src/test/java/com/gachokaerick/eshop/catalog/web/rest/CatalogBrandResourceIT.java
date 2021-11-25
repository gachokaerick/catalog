package com.gachokaerick.eshop.catalog.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.catalog.IntegrationTest;
import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.repository.CatalogBrandRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogBrandDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapper;
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
 * Integration tests for the {@link CatalogBrandResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogBrandResourceIT {

    private static final String DEFAULT_BRAND = "AAAAAAAAAA";
    private static final String UPDATED_BRAND = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/catalog-brands";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CatalogBrandRepository catalogBrandRepository;

    @Autowired
    private CatalogBrandMapper catalogBrandMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogBrandMockMvc;

    private CatalogBrand catalogBrand;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogBrand createEntity(EntityManager em) {
        CatalogBrand catalogBrand = new CatalogBrand().brand(DEFAULT_BRAND);
        return catalogBrand;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogBrand createUpdatedEntity(EntityManager em) {
        CatalogBrand catalogBrand = new CatalogBrand().brand(UPDATED_BRAND);
        return catalogBrand;
    }

    @BeforeEach
    public void initTest() {
        catalogBrand = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogBrand() throws Exception {
        int databaseSizeBeforeCreate = catalogBrandRepository.findAll().size();
        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);
        restCatalogBrandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogBrand testCatalogBrand = catalogBrandList.get(catalogBrandList.size() - 1);
        assertThat(testCatalogBrand.getBrand()).isEqualTo(DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void createCatalogBrandWithExistingId() throws Exception {
        // Create the CatalogBrand with an existing ID
        catalogBrand.setId(1L);
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        int databaseSizeBeforeCreate = catalogBrandRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogBrandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBrandIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogBrandRepository.findAll().size();
        // set the field null
        catalogBrand.setBrand(null);

        // Create the CatalogBrand, which fails.
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        restCatalogBrandMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogBrands() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        // Get all the catalogBrandList
        restCatalogBrandMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogBrand.getId().intValue())))
            .andExpect(jsonPath("$.[*].brand").value(hasItem(DEFAULT_BRAND)));
    }

    @Test
    @Transactional
    void getCatalogBrand() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        // Get the catalogBrand
        restCatalogBrandMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogBrand.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogBrand.getId().intValue()))
            .andExpect(jsonPath("$.brand").value(DEFAULT_BRAND));
    }

    @Test
    @Transactional
    void getNonExistingCatalogBrand() throws Exception {
        // Get the catalogBrand
        restCatalogBrandMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogBrand() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();

        // Update the catalogBrand
        CatalogBrand updatedCatalogBrand = catalogBrandRepository.findById(catalogBrand.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogBrand are not directly saved in db
        em.detach(updatedCatalogBrand);
        updatedCatalogBrand.brand(UPDATED_BRAND);
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(updatedCatalogBrand);

        restCatalogBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogBrandDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
        CatalogBrand testCatalogBrand = catalogBrandList.get(catalogBrandList.size() - 1);
        assertThat(testCatalogBrand.getBrand()).isEqualTo(UPDATED_BRAND);
    }

    @Test
    @Transactional
    void putNonExistingCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogBrandDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogBrandWithPatch() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();

        // Update the catalogBrand using partial update
        CatalogBrand partialUpdatedCatalogBrand = new CatalogBrand();
        partialUpdatedCatalogBrand.setId(catalogBrand.getId());

        restCatalogBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogBrand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogBrand))
            )
            .andExpect(status().isOk());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
        CatalogBrand testCatalogBrand = catalogBrandList.get(catalogBrandList.size() - 1);
        assertThat(testCatalogBrand.getBrand()).isEqualTo(DEFAULT_BRAND);
    }

    @Test
    @Transactional
    void fullUpdateCatalogBrandWithPatch() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();

        // Update the catalogBrand using partial update
        CatalogBrand partialUpdatedCatalogBrand = new CatalogBrand();
        partialUpdatedCatalogBrand.setId(catalogBrand.getId());

        partialUpdatedCatalogBrand.brand(UPDATED_BRAND);

        restCatalogBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogBrand.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogBrand))
            )
            .andExpect(status().isOk());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
        CatalogBrand testCatalogBrand = catalogBrandList.get(catalogBrandList.size() - 1);
        assertThat(testCatalogBrand.getBrand()).isEqualTo(UPDATED_BRAND);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogBrandDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogBrand() throws Exception {
        int databaseSizeBeforeUpdate = catalogBrandRepository.findAll().size();
        catalogBrand.setId(count.incrementAndGet());

        // Create the CatalogBrand
        CatalogBrandDTO catalogBrandDTO = catalogBrandMapper.toDto(catalogBrand);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogBrandMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogBrandDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogBrand in the database
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogBrand() throws Exception {
        // Initialize the database
        catalogBrandRepository.saveAndFlush(catalogBrand);

        int databaseSizeBeforeDelete = catalogBrandRepository.findAll().size();

        // Delete the catalogBrand
        restCatalogBrandMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogBrand.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogBrand> catalogBrandList = catalogBrandRepository.findAll();
        assertThat(catalogBrandList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
