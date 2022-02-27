package com.gachokaerick.eshop.catalog.web.rest;

import static com.gachokaerick.eshop.catalog.config.Constants.RESTOCK_TOPIC;

import com.gachokaerick.eshop.catalog.repository.CatalogItemRepository;
import com.gachokaerick.eshop.catalog.service.CatalogItemService;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing CatalogItems.
 */
@RestController
@RequestMapping("/api")
public class CatalogItemResource {

    private final Logger log = LoggerFactory.getLogger(CatalogItemResource.class);

    private static final String ENTITY_NAME = "catalogCatalogItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogItemService catalogItemService;
    private final CatalogItemRepository catalogItemRepository;
    private final CatalogKafkaResource catalogKafkaResource;

    public CatalogItemResource(
        CatalogItemService catalogItemService,
        CatalogItemRepository catalogItemRepository,
        CatalogKafkaResource catalogKafkaResource
    ) {
        this.catalogItemService = catalogItemService;
        this.catalogItemRepository = catalogItemRepository;
        this.catalogKafkaResource = catalogKafkaResource;
    }

    /**
     * {@code POST  /catalog-items} : Create a new catalogItem.
     *
     * @param catalogItemDTO the catalogItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogItemDTO, or with status {@code 400 (Bad Request)} if the catalogItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-items")
    public ResponseEntity<CatalogItemDTO> createCatalogItem(@Valid @RequestBody CatalogItemDTO catalogItemDTO) throws URISyntaxException {
        log.debug("REST request to save CatalogItem : {}", catalogItemDTO);
        if (catalogItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogItemDTO result = catalogItemService.create(catalogItemDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-items/:id} : Updates an existing catalogItem.
     *
     * @param id             the id of the catalogItemDTO to save.
     * @param catalogItemDTO the catalogItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogItemDTO,
     * or with status {@code 400 (Bad Request)} if the catalogItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-items/{id}")
    public ResponseEntity<CatalogItemDTO> updateCatalogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogItemDTO catalogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogItem : {}, {}", id, catalogItemDTO);
        if (catalogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!Objects.equals(id, catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogItemRepository.existsById(catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogItemDTO result = catalogItemService.update(catalogItemDTO);

        checkThreshold(Optional.of(result));

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-items/:id} : Partial updates given fields of an existing catalogItem, field will ignore if it is null
     *
     * @param id             the id of the catalogItemDTO to save.
     * @param catalogItemDTO the catalogItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogItemDTO,
     * or with status {@code 400 (Bad Request)} if the catalogItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogItemDTO> partialUpdateCatalogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogItemDTO catalogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogItem partially : {}, {}", id, catalogItemDTO);

        if (catalogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogItemRepository.existsById(catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogItemDTO> result = catalogItemService.partialUpdate(catalogItemDTO);

        checkThreshold(result);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-items} : get all the catalogItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogItems in body.
     */
    @GetMapping("/catalog-items")
    public ResponseEntity<List<CatalogItemDTO>> getAllCatalogItems(
        @RequestParam(name = "ids", required = false) String idsString,
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "description", required = false) String description,
        @RequestParam(name = "catalogBrand", required = false) String catalogBrand,
        @RequestParam(name = "catalogType", required = false) String catalogType,
        @RequestParam(name = "term", required = false) String term,
        Pageable pageable
    ) {
        log.debug("REST request to get a page of CatalogItems");
        List<Long> ids = null;
        if (idsString != null) {
            ids = Stream.of(idsString.split(",")).map(NumberUtils::createLong).collect(Collectors.toList());
        }
        Page<CatalogItemDTO> page = catalogItemService.findAll(ids, name, description, catalogBrand, catalogType, term, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-items/:id} : get the "id" catalogItem.
     *
     * @param id the id of the catalogItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-items/{id}")
    public ResponseEntity<CatalogItemDTO> getCatalogItem(@PathVariable Long id) {
        log.debug("REST request to get CatalogItem : {}", id);
        Optional<CatalogItemDTO> catalogItemDTO = catalogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogItemDTO);
    }

    /**
     * {@code DELETE  /catalog-items/:id} : delete the "id" catalogItem.
     *
     * @param id the id of the catalogItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-items/{id}")
    public ResponseEntity<Void> deleteCatalogItem(@PathVariable Long id) {
        log.debug("REST request to delete CatalogItem : {}", id);
        catalogItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @PatchMapping(value = "/catalog-items/add/{quantity}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogItemDTO> partialUpdateCatalogItemAddStock(
        @PathVariable(value = "quantity") final Integer quantity,
        @NotNull @RequestBody CatalogItemDTO catalogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to add stock to CatalogItem partially : {}", catalogItemDTO);

        if (catalogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (quantity == null || quantity <= 0) {
            throw new BadRequestAlertException("Quantity to add to stock must be greater than zero", ENTITY_NAME, "quantityInvalid");
        }

        if (!catalogItemRepository.existsById(catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idNotFound");
        }

        Optional<CatalogItemDTO> result = catalogItemService.partialUpdateAddStock(catalogItemDTO, quantity);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItemDTO.getId().toString())
        );
    }

    @PatchMapping(value = "/catalog-items/remove/{quantity}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogItemDTO> partialUpdateCatalogItemRemoveStock(
        @PathVariable(value = "quantity") final Integer quantity,
        @NotNull @RequestBody CatalogItemDTO catalogItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to remove stock to CatalogItem partially : {}", catalogItemDTO);

        if (catalogItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (quantity == null || quantity <= 0) {
            throw new BadRequestAlertException("Quantity to remove to stock must be greater than zero", ENTITY_NAME, "quantityInvalid");
        }

        if (!catalogItemRepository.existsById(catalogItemDTO.getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idNotFound");
        }

        Optional<CatalogItemDTO> result = catalogItemService.partialUpdateRemoveStock(catalogItemDTO, quantity);

        checkThreshold(result);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItemDTO.getId().toString())
        );
    }

    private void checkThreshold(Optional<CatalogItemDTO> catalogItemDTOOptional) {
        if (catalogItemDTOOptional.isPresent()) {
            // send email to admin if stock reaches restock threshold
            CatalogItemDTO catalogItemDTO = catalogItemDTOOptional.get();

            if (Objects.equals(catalogItemDTO.getAvailableStock(), catalogItemDTO.getRestockThreshold())) {
                String message = "Item: " + catalogItemDTO.getName() + " has reached restock threshold";
                try {
                    catalogKafkaResource.publish(RESTOCK_TOPIC, message, null);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
