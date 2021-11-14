package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.CatalogType;
import com.gachokaerick.eshop.catalog.repository.CatalogTypeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogType}.
 */
@Service
@Transactional
public class CatalogTypeService {

    private final Logger log = LoggerFactory.getLogger(CatalogTypeService.class);

    private final CatalogTypeRepository catalogTypeRepository;

    public CatalogTypeService(CatalogTypeRepository catalogTypeRepository) {
        this.catalogTypeRepository = catalogTypeRepository;
    }

    /**
     * Save a catalogType.
     *
     * @param catalogType the entity to save.
     * @return the persisted entity.
     */
    public CatalogType save(CatalogType catalogType) {
        log.debug("Request to save CatalogType : {}", catalogType);
        return catalogTypeRepository.save(catalogType);
    }

    /**
     * Partially update a catalogType.
     *
     * @param catalogType the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogType> partialUpdate(CatalogType catalogType) {
        log.debug("Request to partially update CatalogType : {}", catalogType);

        return catalogTypeRepository
            .findById(catalogType.getId())
            .map(existingCatalogType -> {
                if (catalogType.getType() != null) {
                    existingCatalogType.setType(catalogType.getType());
                }

                return existingCatalogType;
            })
            .map(catalogTypeRepository::save);
    }

    /**
     * Get all the catalogTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogType> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogTypes");
        return catalogTypeRepository.findAll(pageable);
    }

    /**
     * Get one catalogType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogType> findOne(Long id) {
        log.debug("Request to get CatalogType : {}", id);
        return catalogTypeRepository.findById(id);
    }

    /**
     * Delete the catalogType by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogType : {}", id);
        catalogTypeRepository.deleteById(id);
    }
}
