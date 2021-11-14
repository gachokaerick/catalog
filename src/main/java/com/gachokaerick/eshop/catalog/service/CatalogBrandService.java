package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.CatalogBrand;
import com.gachokaerick.eshop.catalog.repository.CatalogBrandRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CatalogBrand}.
 */
@Service
@Transactional
public class CatalogBrandService {

    private final Logger log = LoggerFactory.getLogger(CatalogBrandService.class);

    private final CatalogBrandRepository catalogBrandRepository;

    public CatalogBrandService(CatalogBrandRepository catalogBrandRepository) {
        this.catalogBrandRepository = catalogBrandRepository;
    }

    /**
     * Save a catalogBrand.
     *
     * @param catalogBrand the entity to save.
     * @return the persisted entity.
     */
    public CatalogBrand save(CatalogBrand catalogBrand) {
        log.debug("Request to save CatalogBrand : {}", catalogBrand);
        return catalogBrandRepository.save(catalogBrand);
    }

    /**
     * Partially update a catalogBrand.
     *
     * @param catalogBrand the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogBrand> partialUpdate(CatalogBrand catalogBrand) {
        log.debug("Request to partially update CatalogBrand : {}", catalogBrand);

        return catalogBrandRepository
            .findById(catalogBrand.getId())
            .map(existingCatalogBrand -> {
                if (catalogBrand.getBrand() != null) {
                    existingCatalogBrand.setBrand(catalogBrand.getBrand());
                }

                return existingCatalogBrand;
            })
            .map(catalogBrandRepository::save);
    }

    /**
     * Get all the catalogBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogBrand> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogBrands");
        return catalogBrandRepository.findAll(pageable);
    }

    /**
     * Get one catalogBrand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogBrand> findOne(Long id) {
        log.debug("Request to get CatalogBrand : {}", id);
        return catalogBrandRepository.findById(id);
    }

    /**
     * Delete the catalogBrand by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogBrand : {}", id);
        catalogBrandRepository.deleteById(id);
    }
}
