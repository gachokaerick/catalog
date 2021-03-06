package com.gachokaerick.eshop.catalog.web.rest;

import static com.gachokaerick.eshop.catalog.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gachokaerick.eshop.catalog.IntegrationTest;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemDomain;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapper;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapperImpl;
import com.gachokaerick.eshop.catalog.exception.DomainException;
import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.model.CatalogType;
import com.gachokaerick.eshop.catalog.repository.CatalogItemRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.service.mapper.*;
import java.math.BigDecimal;
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
 * Integration tests for the {@link CatalogItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CatalogItemResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final String DEFAULT_PICTURE_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PICTURE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PICTURE_URL = "BBBBBBBBBB";

    private static final Integer DEFAULT_AVAILABLE_STOCK = 1;
    private static final Integer UPDATED_AVAILABLE_STOCK = 2;

    private static final Integer DEFAULT_RESTOCK_THRESHOLD = 2;
    private static final Integer UPDATED_RESTOCK_THRESHOLD = 3;

    private static final Integer DEFAULT_MAX_STOCK_THRESHOLD = 3;
    private static final Integer UPDATED_MAX_STOCK_THRESHOLD = 4;

    private static final Boolean DEFAULT_ON_REORDER = false;
    private static final Boolean UPDATED_ON_REORDER = true;

    private static final String ENTITY_API_URL = "/api/catalog-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_API_URL_ID_ADD_STOCK = ENTITY_API_URL + "/add/{quantity}";
    private static final String ENTITY_API_URL_ID_REMOVE_STOCK = ENTITY_API_URL + "/remove/{quantity}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private CatalogItemRepository catalogItemRepository;

    @Autowired
    private CatalogItemMapper catalogItemMapper;

    @Autowired
    private CatalogBrandMapper catalogBrandMapper;

    @Autowired
    private CatalogTypeMapper catalogTypeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCatalogItemMockMvc;

    private CatalogItem catalogItem;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CatalogItem createEntity(EntityManager em) {
        CatalogItemMapper catalogItemMapper = new CatalogItemMapperImpl();
        CatalogBrandMapper catalogBrandMapper = new CatalogBrandMapperImpl();
        CatalogTypeMapper catalogTypeMapper = new CatalogTypeMapperImpl();

        // Add required entity
        CatalogBrand catalogBrand;
        if (TestUtil.findAll(em, CatalogBrand.class).isEmpty()) {
            catalogBrand = CatalogBrandResourceIT.createEntity(em);
            em.persist(catalogBrand);
            em.flush();
        } else {
            catalogBrand = TestUtil.findAll(em, CatalogBrand.class).get(0);
        }
        // Add required entity
        CatalogType catalogType;
        if (TestUtil.findAll(em, CatalogType.class).isEmpty()) {
            catalogType = CatalogTypeResourceIT.createEntity(em);
            em.persist(catalogType);
            em.flush();
        } else {
            catalogType = TestUtil.findAll(em, CatalogType.class).get(0);
        }

        CatalogItemDTO catalogItem = new CatalogItemDTO(
            null,
            DEFAULT_NAME,
            DEFAULT_DESCRIPTION,
            DEFAULT_PRICE,
            DEFAULT_PICTURE_FILE_NAME,
            DEFAULT_PICTURE_URL,
            DEFAULT_AVAILABLE_STOCK,
            DEFAULT_RESTOCK_THRESHOLD,
            DEFAULT_MAX_STOCK_THRESHOLD,
            DEFAULT_ON_REORDER,
            catalogBrandMapper.toDto(catalogBrand),
            catalogTypeMapper.toDto(catalogType)
        );

        return new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItem).build().getCatalogItem();
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public CatalogItem createUpdatedEntity(EntityManager em) {
        CatalogBrand catalogBrand;
        if (TestUtil.findAll(em, CatalogBrand.class).isEmpty()) {
            catalogBrand = CatalogBrandResourceIT.createEntity(em);
            em.persist(catalogBrand);
            em.flush();
        } else {
            catalogBrand = TestUtil.findAll(em, CatalogBrand.class).get(0);
        }
        // Add required entity
        CatalogType catalogType;
        if (TestUtil.findAll(em, CatalogType.class).isEmpty()) {
            catalogType = CatalogTypeResourceIT.createEntity(em);
            em.persist(catalogType);
            em.flush();
        } else {
            catalogType = TestUtil.findAll(em, CatalogType.class).get(0);
        }

        CatalogItemDTO catalogItem = new CatalogItemDTO(
            null,
            UPDATED_NAME,
            UPDATED_DESCRIPTION,
            UPDATED_PRICE,
            UPDATED_PICTURE_FILE_NAME,
            UPDATED_PICTURE_URL,
            UPDATED_AVAILABLE_STOCK,
            UPDATED_RESTOCK_THRESHOLD,
            UPDATED_MAX_STOCK_THRESHOLD,
            UPDATED_ON_REORDER,
            catalogBrandMapper.toDto(catalogBrand),
            catalogTypeMapper.toDto(catalogType)
        );

        return new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItem).build().getCatalogItem();
    }

    @BeforeEach
    public void initTest() {
        catalogItem = createEntity(em);
    }

    @Test
    @Transactional
    void createCatalogItem() throws Exception {
        int databaseSizeBeforeCreate = catalogItemRepository.findAll().size();
        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isCreated());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeCreate + 1);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCatalogItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogItem.getPrice()).isEqualByComparingTo(DEFAULT_PRICE);
        assertThat(testCatalogItem.getPictureFileName()).isEqualTo(DEFAULT_PICTURE_FILE_NAME);
        assertThat(testCatalogItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(DEFAULT_AVAILABLE_STOCK);
        assertThat(testCatalogItem.getRestockThreshold()).isEqualTo(DEFAULT_RESTOCK_THRESHOLD);
        assertThat(testCatalogItem.getMaxStockThreshold()).isEqualTo(DEFAULT_MAX_STOCK_THRESHOLD);
        assertThat(testCatalogItem.getOnReorder()).isEqualTo(DEFAULT_ON_REORDER);
    }

    @Test
    @Transactional
    void createCatalogItemWithExistingId() throws Exception {
        // Create the CatalogItem with an existing ID
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(1L);

        int databaseSizeBeforeCreate = catalogItemRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogItemRepository.findAll().size();

        // Create the CatalogItem, which fails.
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        // set the field null
        catalogItemDTO.setName(null);

        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogItemRepository.findAll().size();

        // Create the CatalogItem, which fails.
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        // set the field null
        catalogItemDTO.setPrice(null);

        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAvailableStockIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogItemRepository.findAll().size();

        // Create the CatalogItem, which fails.
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        // set the field null
        catalogItemDTO.setAvailableStock(null);

        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRestockThresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogItemRepository.findAll().size();

        // Create the CatalogItem, which fails.
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        // set the field null
        catalogItemDTO.setRestockThreshold(null);

        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMaxStockThresholdIsRequired() throws Exception {
        int databaseSizeBeforeTest = catalogItemRepository.findAll().size();

        // Create the CatalogItem, which fails.
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        // set the field null
        catalogItemDTO.setMaxStockThreshold(null);

        restCatalogItemMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCatalogItems() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        // Get all the catalogItemList
        restCatalogItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(catalogItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].pictureFileName").value(hasItem(DEFAULT_PICTURE_FILE_NAME)))
            .andExpect(jsonPath("$.[*].pictureUrl").value(hasItem(DEFAULT_PICTURE_URL)))
            .andExpect(jsonPath("$.[*].availableStock").value(hasItem(DEFAULT_AVAILABLE_STOCK)))
            .andExpect(jsonPath("$.[*].restockThreshold").value(hasItem(DEFAULT_RESTOCK_THRESHOLD)))
            .andExpect(jsonPath("$.[*].maxStockThreshold").value(hasItem(DEFAULT_MAX_STOCK_THRESHOLD)))
            .andExpect(jsonPath("$.[*].onReorder").value(hasItem(DEFAULT_ON_REORDER.booleanValue())));
    }

    @Test
    @Transactional
    void getCatalogItem() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        // Get the catalogItem
        restCatalogItemMockMvc
            .perform(get(ENTITY_API_URL_ID, catalogItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(catalogItem.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.pictureFileName").value(DEFAULT_PICTURE_FILE_NAME))
            .andExpect(jsonPath("$.pictureUrl").value(DEFAULT_PICTURE_URL))
            .andExpect(jsonPath("$.availableStock").value(DEFAULT_AVAILABLE_STOCK))
            .andExpect(jsonPath("$.restockThreshold").value(DEFAULT_RESTOCK_THRESHOLD))
            .andExpect(jsonPath("$.maxStockThreshold").value(DEFAULT_MAX_STOCK_THRESHOLD))
            .andExpect(jsonPath("$.onReorder").value(DEFAULT_ON_REORDER.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingCatalogItem() throws Exception {
        // Get the catalogItem
        restCatalogItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCatalogItem() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Update the catalogItem
        CatalogItem updatedCatalogItem = catalogItemRepository.findById(catalogItem.getId()).get();
        // Disconnect from session so that the updates on updatedCatalogItem are not directly saved in db
        em.detach(updatedCatalogItem);

        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(updatedCatalogItem);
        catalogItemDTO
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .pictureFileName(UPDATED_PICTURE_FILE_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .availableStock(UPDATED_AVAILABLE_STOCK)
            .restockThreshold(UPDATED_RESTOCK_THRESHOLD)
            .maxStockThreshold(UPDATED_MAX_STOCK_THRESHOLD)
            .onReorder(UPDATED_ON_REORDER);

        restCatalogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testCatalogItem.getPictureFileName()).isEqualTo(UPDATED_PICTURE_FILE_NAME);
        assertThat(testCatalogItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(UPDATED_AVAILABLE_STOCK);
        assertThat(testCatalogItem.getRestockThreshold()).isEqualTo(UPDATED_RESTOCK_THRESHOLD);
        assertThat(testCatalogItem.getMaxStockThreshold()).isEqualTo(UPDATED_MAX_STOCK_THRESHOLD);
        assertThat(testCatalogItem.getOnReorder()).isEqualTo(UPDATED_ON_REORDER);
    }

    @Test
    @Transactional
    void putNonExistingCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, catalogItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCatalogItemWithPatch() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Update the catalogItem using partial update
        CatalogItemDTO partialUpdatedCatalogItem = catalogItemMapper.toDto(catalogItem);
        partialUpdatedCatalogItem.setId(catalogItem.getId());

        partialUpdatedCatalogItem
            .name(UPDATED_NAME)
            .price(UPDATED_PRICE)
            .restockThreshold(UPDATED_RESTOCK_THRESHOLD)
            .maxStockThreshold(UPDATED_MAX_STOCK_THRESHOLD);

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogItem))
            )
            .andExpect(status().isOk());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCatalogItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testCatalogItem.getPictureFileName()).isEqualTo(DEFAULT_PICTURE_FILE_NAME);
        assertThat(testCatalogItem.getPictureUrl()).isEqualTo(DEFAULT_PICTURE_URL);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(DEFAULT_AVAILABLE_STOCK);
        assertThat(testCatalogItem.getRestockThreshold()).isEqualTo(UPDATED_RESTOCK_THRESHOLD);
        assertThat(testCatalogItem.getMaxStockThreshold()).isEqualTo(UPDATED_MAX_STOCK_THRESHOLD);
        assertThat(testCatalogItem.getOnReorder()).isEqualTo(DEFAULT_ON_REORDER);
    }

    @Test
    @Transactional
    void fullUpdateCatalogItemWithPatch() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Update the catalogItem using partial update
        CatalogItemDTO partialUpdatedCatalogItem = catalogItemMapper.toDto(catalogItem);
        partialUpdatedCatalogItem.setId(catalogItem.getId());

        partialUpdatedCatalogItem
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .price(UPDATED_PRICE)
            .pictureFileName(UPDATED_PICTURE_FILE_NAME)
            .pictureUrl(UPDATED_PICTURE_URL)
            .availableStock(UPDATED_AVAILABLE_STOCK)
            .restockThreshold(UPDATED_RESTOCK_THRESHOLD)
            .maxStockThreshold(UPDATED_MAX_STOCK_THRESHOLD)
            .onReorder(UPDATED_ON_REORDER);

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCatalogItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCatalogItem))
            )
            .andExpect(status().isOk());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCatalogItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCatalogItem.getPrice()).isEqualByComparingTo(UPDATED_PRICE);
        assertThat(testCatalogItem.getPictureFileName()).isEqualTo(UPDATED_PICTURE_FILE_NAME);
        assertThat(testCatalogItem.getPictureUrl()).isEqualTo(UPDATED_PICTURE_URL);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(UPDATED_AVAILABLE_STOCK);
        assertThat(testCatalogItem.getRestockThreshold()).isEqualTo(UPDATED_RESTOCK_THRESHOLD);
        assertThat(testCatalogItem.getMaxStockThreshold()).isEqualTo(UPDATED_MAX_STOCK_THRESHOLD);
        assertThat(testCatalogItem.getOnReorder()).isEqualTo(UPDATED_ON_REORDER);
    }

    @Test
    @Transactional
    void patchNonExistingCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, catalogItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCatalogItem() throws Exception {
        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        // Create the CatalogItem
        CatalogItemDTO catalogItemDTO = catalogItemMapper.toDto(catalogItem);
        catalogItemDTO.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CatalogItem in the database
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCatalogItem() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeDelete = catalogItemRepository.findAll().size();

        // Delete the catalogItem
        restCatalogItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, catalogItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    void addStockAboveMaxThreshold() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_ADD_STOCK, catalogItem.getMaxStockThreshold() + 1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(catalogItem.getMaxStockThreshold());
    }

    @Test
    @Transactional
    void addStockEqualToMaxThreshold() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_ADD_STOCK, catalogItem.getMaxStockThreshold())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(catalogItem.getMaxStockThreshold());
    }

    @Test
    @Transactional
    void addStockBelowMaxThreshold() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_ADD_STOCK, 1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getId()).isEqualTo(catalogItem.getId());
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(DEFAULT_AVAILABLE_STOCK + 1);
    }

    @Test
    @Transactional
    void addStockWithZeroQuantity() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_ADD_STOCK, 0)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void addStockWithNegativeQuantity() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_ADD_STOCK, -1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void testStockRemovalWithNegativeQuantity() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_REMOVE_STOCK, -1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void testStockRemovalWithZeroQuantity() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_REMOVE_STOCK, 0)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isBadRequest());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void testStockRemoval() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_REMOVE_STOCK, 1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getId()).isEqualTo(catalogItem.getId());
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(DEFAULT_AVAILABLE_STOCK - 1);
    }

    @Test
    @Transactional
    void testStockRemovalWithQuantityEqualToAvailableStock() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_REMOVE_STOCK, DEFAULT_AVAILABLE_STOCK)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getId()).isEqualTo(catalogItem.getId());
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(0);
    }

    @Test
    @Transactional
    void testStockRemovalWithQuantityMoreThanAvailableStock() throws Exception {
        // Initialize the database
        catalogItemRepository.saveAndFlush(catalogItem);

        int databaseSizeBeforeUpdate = catalogItemRepository.findAll().size();

        CatalogItemDTO catalogItemDTO = new CatalogItemDTO();
        catalogItemDTO.setId(catalogItem.getId());

        restCatalogItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID_REMOVE_STOCK, DEFAULT_AVAILABLE_STOCK + 1)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(catalogItemDTO))
            )
            .andExpect(status().isOk());

        List<CatalogItem> catalogItemList = catalogItemRepository.findAll();
        assertThat(catalogItemList).hasSize(databaseSizeBeforeUpdate);
        CatalogItem testCatalogItem = catalogItemList.get(catalogItemList.size() - 1);
        assertThat(testCatalogItem.getId()).isEqualTo(catalogItem.getId());
        assertThat(testCatalogItem.getAvailableStock()).isEqualTo(0);
    }
}
