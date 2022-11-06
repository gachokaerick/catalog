package com.gachokaerick.eshop.catalog.web.rest;

import com.gachokaerick.eshop.catalog.repository.CatalogTypeRepository;
import com.gachokaerick.eshop.catalog.service.CatalogTypeService;
import com.gachokaerick.eshop.catalog.service.dto.CatalogTypeDTO;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gachokaerick.eshop.catalog.model.CatalogType}.
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
     * @param catalogTypeDTO the catalogTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new catalogTypeDTO, or with status {@code 400 (Bad Request)} if the catalogType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/catalog-types")
    public ResponseEntity<CatalogTypeDTO> createCatalogType(@Valid @RequestBody CatalogTypeDTO catalogTypeDTO) throws URISyntaxException {
        log.debug("REST request to save CatalogType : {}", catalogTypeDTO);
        if (catalogTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new catalogType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogTypeDTO result = catalogTypeService.save(catalogTypeDTO);
        return ResponseEntity
            .created(new URI("/api/catalog-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /catalog-types/:id} : Updates an existing catalogType.
     *
     * @param id the id of the catalogTypeDTO to save.
     * @param catalogTypeDTO the catalogTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogTypeDTO,
     * or with status {@code 400 (Bad Request)} if the catalogTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the catalogTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/catalog-types/{id}")
    public ResponseEntity<CatalogTypeDTO> updateCatalogType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CatalogTypeDTO catalogTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CatalogType : {}, {}", id, catalogTypeDTO);
        if (catalogTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogTypeDTO result = catalogTypeService.save(catalogTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /catalog-types/:id} : Partial updates given fields of an existing catalogType, field will ignore if it is null
     *
     * @param id the id of the catalogTypeDTO to save.
     * @param catalogTypeDTO the catalogTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated catalogTypeDTO,
     * or with status {@code 400 (Bad Request)} if the catalogTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the catalogTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the catalogTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/catalog-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CatalogTypeDTO> partialUpdateCatalogType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CatalogTypeDTO catalogTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CatalogType partially : {}, {}", id, catalogTypeDTO);
        if (catalogTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, catalogTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!catalogTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CatalogTypeDTO> result = catalogTypeService.partialUpdate(catalogTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, catalogTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /catalog-types} : get all the catalogTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of catalogTypes in body.
     */
    @GetMapping("/catalog-types")
    public ResponseEntity<List<CatalogTypeDTO>> getAllCatalogTypes(Pageable pageable) {
        log.debug("REST request to get a page of CatalogTypes");
        Page<CatalogTypeDTO> page = catalogTypeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /catalog-types/:id} : get the "id" catalogType.
     *
     * @param id the id of the catalogTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the catalogTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/catalog-types/{id}")
    public ResponseEntity<CatalogTypeDTO> getCatalogType(@PathVariable Long id) {
        log.debug("REST request to get CatalogType : {}", id);
        Optional<CatalogTypeDTO> catalogTypeDTO = catalogTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(catalogTypeDTO);
    }

    /**
     * {@code DELETE  /catalog-types/:id} : delete the "id" catalogType.
     *
     * @param id the id of the catalogTypeDTO to delete.
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
