package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.CatalogBrand;
import com.gachokaerick.eshop.catalog.repository.CatalogBrandRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogBrandDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapper;
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

    private final CatalogBrandMapper catalogBrandMapper;

    public CatalogBrandService(CatalogBrandRepository catalogBrandRepository, CatalogBrandMapper catalogBrandMapper) {
        this.catalogBrandRepository = catalogBrandRepository;
        this.catalogBrandMapper = catalogBrandMapper;
    }

    /**
     * Save a catalogBrand.
     *
     * @param catalogBrandDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogBrandDTO save(CatalogBrandDTO catalogBrandDTO) {
        log.debug("Request to save CatalogBrand : {}", catalogBrandDTO);
        CatalogBrand catalogBrand = catalogBrandMapper.toEntity(catalogBrandDTO);
        catalogBrand = catalogBrandRepository.save(catalogBrand);
        return catalogBrandMapper.toDto(catalogBrand);
    }

    /**
     * Partially update a catalogBrand.
     *
     * @param catalogBrandDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogBrandDTO> partialUpdate(CatalogBrandDTO catalogBrandDTO) {
        log.debug("Request to partially update CatalogBrand : {}", catalogBrandDTO);

        return catalogBrandRepository
            .findById(catalogBrandDTO.getId())
            .map(existingCatalogBrand -> {
                catalogBrandMapper.partialUpdate(existingCatalogBrand, catalogBrandDTO);

                return existingCatalogBrand;
            })
            .map(catalogBrandRepository::save)
            .map(catalogBrandMapper::toDto);
    }

    /**
     * Get all the catalogBrands.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogBrandDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogBrands");
        return catalogBrandRepository.findAll(pageable).map(catalogBrandMapper::toDto);
    }

    /**
     * Get one catalogBrand by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogBrandDTO> findOne(Long id) {
        log.debug("Request to get CatalogBrand : {}", id);
        return catalogBrandRepository.findById(id).map(catalogBrandMapper::toDto);
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
