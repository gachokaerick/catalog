package com.gachokaerick.eshop.catalog.web.rest;

import com.gachokaerick.eshop.catalog.domain.CatalogItem;
import com.gachokaerick.eshop.catalog.repository.CatalogItemRepository;
import com.gachokaerick.eshop.catalog.service.CatalogItemService;
import com.gachokaerick.eshop.catalog.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gachokaerick.eshop.catalog.domain.CatalogItem}.
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

    public CatalogItemResource(CatalogItemService catalogItemService, CatalogItemRepository catalogItemRepository) {
        this.catalogItemService = catalogItemService;
        this.catalogItemRepository = catalogItemRepository;
    }

    /**
     * {@code POST  /catalog-items} : Create a new catalogItem.
     *
     * @param catalogItem the catalogItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogItem, or with status {@code 400 (Bad Request)} if the catalogItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-items")
    public ResponseEntity<CatalogItem> createCatalogItem(@Valid @RequestBody CatalogItem catalogItem) throws URISyntaxException {
        log.debug("REST request to save CatalogItem : {}", catalogItem);
        if (catalogItem.getId() != null) {
            throw new BadRequestAlertException("A new catalogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogItem result = catalogItemService.save(catalogItem);
        return ResponseEntity
            .created(new URI("/api/catalog-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-items/:id} : Updates an existing catalogItem.
     *
     * @param id the id of the catalogItem to save.
     * @param catalogItem the catalogItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogItem,
     * or with status {@code 400 (Bad Request)} if the catalogItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-items/{id}")
    public ResponseEntity<CatalogItem> updateCatalogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogItem catalogItem
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogItem : {}, {}", id, catalogItem);
        if (catalogItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogItem result = catalogItemService.save(catalogItem);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItem.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-items/:id} : Partial updates given fields of an existing catalogItem, field will ignore if it is null
     *
     * @param id the id of the catalogItem to save.
     * @param catalogItem the catalogItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogItem,
     * or with status {@code 400 (Bad Request)} if the catalogItem is not valid,
     * or with status {@code 404 (Not Found)} if the catalogItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogItem> partialUpdateCatalogItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogItem catalogItem
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogItem partially : {}, {}", id, catalogItem);
        if (catalogItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogItem> result = catalogItemService.partialUpdate(catalogItem);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogItem.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-items} : get all the catalogItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogItems in body.
     */
    @GetMapping("/catalog-items")
    public ResponseEntity<List<CatalogItem>> getAllCatalogItems(Pageable pageable) {
        log.debug("REST request to get a page of CatalogItems");
        Page<CatalogItem> page = catalogItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-items/:id} : get the "id" catalogItem.
     *
     * @param id the id of the catalogItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-items/{id}")
    public ResponseEntity<CatalogItem> getCatalogItem(@PathVariable Long id) {
        log.debug("REST request to get CatalogItem : {}", id);
        Optional<CatalogItem> catalogItem = catalogItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogItem);
    }

    /**
     * {@code DELETE  /catalog-items/:id} : delete the "id" catalogItem.
     *
     * @param id the id of the catalogItem to delete.
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
}
