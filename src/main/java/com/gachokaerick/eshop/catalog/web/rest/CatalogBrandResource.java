package com.gachokaerick.eshop.catalog.web.rest;

import com.gachokaerick.eshop.catalog.domain.CatalogBrand;
import com.gachokaerick.eshop.catalog.repository.CatalogBrandRepository;
import com.gachokaerick.eshop.catalog.service.CatalogBrandService;
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
 * REST controller for managing {@link com.gachokaerick.eshop.catalog.domain.CatalogBrand}.
 */
@RestController
@RequestMapping("/api")
public class CatalogBrandResource {

    private final Logger log = LoggerFactory.getLogger(CatalogBrandResource.class);

    private static final String ENTITY_NAME = "catalogCatalogBrand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CatalogBrandService catalogBrandService;

    private final CatalogBrandRepository catalogBrandRepository;

    public CatalogBrandResource(CatalogBrandService catalogBrandService, CatalogBrandRepository catalogBrandRepository) {
        this.catalogBrandService = catalogBrandService;
        this.catalogBrandRepository = catalogBrandRepository;
    }

    /**
     * {@code POST  /catalog-brands} : Create a new catalogBrand.
     *
     * @param catalogBrand the catalogBrand to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogBrand, or with status {@code 400 (Bad Request)} if the catalogBrand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-brands")
    public ResponseEntity<CatalogBrand> createCatalogBrand(@Valid @RequestBody CatalogBrand catalogBrand) throws URISyntaxException {
        log.debug("REST request to save CatalogBrand : {}", catalogBrand);
        if (catalogBrand.getId() != null) {
            throw new BadRequestAlertException("A new catalogBrand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogBrand result = catalogBrandService.save(catalogBrand);
        return ResponseEntity
            .created(new URI("/api/catalog-brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-brands/:id} : Updates an existing catalogBrand.
     *
     * @param id the id of the catalogBrand to save.
     * @param catalogBrand the catalogBrand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogBrand,
     * or with status {@code 400 (Bad Request)} if the catalogBrand is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogBrand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-brands/{id}")
    public ResponseEntity<CatalogBrand> updateCatalogBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogBrand catalogBrand
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogBrand : {}, {}", id, catalogBrand);
        if (catalogBrand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogBrand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogBrand result = catalogBrandService.save(catalogBrand);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogBrand.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-brands/:id} : Partial updates given fields of an existing catalogBrand, field will ignore if it is null
     *
     * @param id the id of the catalogBrand to save.
     * @param catalogBrand the catalogBrand to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogBrand,
     * or with status {@code 400 (Bad Request)} if the catalogBrand is not valid,
     * or with status {@code 404 (Not Found)} if the catalogBrand is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogBrand couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-brands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogBrand> partialUpdateCatalogBrand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogBrand catalogBrand
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogBrand partially : {}, {}", id, catalogBrand);
        if (catalogBrand.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogBrand.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogBrandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogBrand> result = catalogBrandService.partialUpdate(catalogBrand);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogBrand.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-brands} : get all the catalogBrands.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogBrands in body.
     */
    @GetMapping("/catalog-brands")
    public ResponseEntity<List<CatalogBrand>> getAllCatalogBrands(Pageable pageable) {
        log.debug("REST request to get a page of CatalogBrands");
        Page<CatalogBrand> page = catalogBrandService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-brands/:id} : get the "id" catalogBrand.
     *
     * @param id the id of the catalogBrand to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogBrand, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-brands/{id}")
    public ResponseEntity<CatalogBrand> getCatalogBrand(@PathVariable Long id) {
        log.debug("REST request to get CatalogBrand : {}", id);
        Optional<CatalogBrand> catalogBrand = catalogBrandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogBrand);
    }

    /**
     * {@code DELETE  /catalog-brands/:id} : delete the "id" catalogBrand.
     *
     * @param id the id of the catalogBrand to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/catalog-brands/{id}")
    public ResponseEntity<Void> deleteCatalogBrand(@PathVariable Long id) {
        log.debug("REST request to delete CatalogBrand : {}", id);
        catalogBrandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
