package com.gachokaerick.eshop.catalog.web.rest;

import com.gachokaerick.eshop.catalog.domain.CatalogType;
import com.gachokaerick.eshop.catalog.repository.CatalogTypeRepository;
import com.gachokaerick.eshop.catalog.service.CatalogTypeService;
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
 * REST controller for managing {@link com.gachokaerick.eshop.catalog.domain.CatalogType}.
 */
@RestController
@RequestMapping("/api")
public class CatalogTypeResource {

    private final Logger log = LoggerFactory.getLogger(CatalogTypeResource.class);

    private static final String ENTITY_NAME = "catalogCatalogType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogTypeService catalogTypeService;

    private final CatalogTypeRepository catalogTypeRepository;

    public CatalogTypeResource(CatalogTypeService catalogTypeService, CatalogTypeRepository catalogTypeRepository) {
        this.catalogTypeService = catalogTypeService;
        this.catalogTypeRepository = catalogTypeRepository;
    }

    /**
     * {@code POST  /catalog-types} : Create a new catalogType.
     *
     * @param catalogType the catalogType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogType, or with status {@code 400 (Bad Request)} if the catalogType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-types")
    public ResponseEntity<CatalogType> createCatalogType(@Valid @RequestBody CatalogType catalogType) throws URISyntaxException {
        log.debug("REST request to save CatalogType : {}", catalogType);
        if (catalogType.getId() != null) {
            throw new BadRequestAlertException("A new catalogType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogType result = catalogTypeService.save(catalogType);
        return ResponseEntity
            .created(new URI("/api/catalog-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-types/:id} : Updates an existing catalogType.
     *
     * @param id the id of the catalogType to save.
     * @param catalogType the catalogType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogType,
     * or with status {@code 400 (Bad Request)} if the catalogType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-types/{id}")
    public ResponseEntity<CatalogType> updateCatalogType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogType catalogType
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogType : {}, {}", id, catalogType);
        if (catalogType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogType result = catalogTypeService.save(catalogType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-types/:id} : Partial updates given fields of an existing catalogType, field will ignore if it is null
     *
     * @param id the id of the catalogType to save.
     * @param catalogType the catalogType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogType,
     * or with status {@code 400 (Bad Request)} if the catalogType is not valid,
     * or with status {@code 404 (Not Found)} if the catalogType is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogType> partialUpdateCatalogType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogType catalogType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogType partially : {}, {}", id, catalogType);
        if (catalogType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogType> result = catalogTypeService.partialUpdate(catalogType);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogType.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-types} : get all the catalogTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogTypes in body.
     */
    @GetMapping("/catalog-types")
    public ResponseEntity<List<CatalogType>> getAllCatalogTypes(Pageable pageable) {
        log.debug("REST request to get a page of CatalogTypes");
        Page<CatalogType> page = catalogTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-types/:id} : get the "id" catalogType.
     *
     * @param id the id of the catalogType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-types/{id}")
    public ResponseEntity<CatalogType> getCatalogType(@PathVariable Long id) {
        log.debug("REST request to get CatalogType : {}", id);
        Optional<CatalogType> catalogType = catalogTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogType);
    }

    /**
     * {@code DELETE  /catalog-types/:id} : delete the "id" catalogType.
     *
     * @param id the id of the catalogType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-types/{id}")
    public ResponseEntity<Void> deleteCatalogType(@PathVariable Long id) {
        log.debug("REST request to delete CatalogType : {}", id);
        catalogTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
