package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemDomain;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemDomainRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service Implementation for managing CatalogItems.
 */
@Service
public class CatalogItemService {

    private final Logger log = LoggerFactory.getLogger(CatalogItemService.class);

    private final CatalogItemDomainRepository catalogItemDomainRepository;

    public CatalogItemService(CatalogItemDomainRepository catalogItemDomainRepository) {
        this.catalogItemDomainRepository = catalogItemDomainRepository;
    }

    /**
     * Save a catalogItem.
     *
     * @param catalogItemDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogItemDTO create(CatalogItemDTO catalogItemDTO) {
        log.debug("Request to create a CatalogItem : {}", catalogItemDTO);
        CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItemDTO).build();
        return catalogItemDomainRepository.create(catalogItemDomain);
    }

    public CatalogItemDTO update(CatalogItemDTO catalogItemDTO) {
        log.debug("Request to update a CatalogItem : {}", catalogItemDTO);
        CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItemDTO).build();
        return catalogItemDomainRepository.update(catalogItemDomain);
    }

    /**
     * Partially update a catalogItem.
     *
     * @param catalogItemDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogItemDTO> partialUpdate(CatalogItemDTO catalogItemDTO) {
        log.debug("Request to partially update CatalogItem : {}", catalogItemDTO);
        CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItemDTO).build();
        return catalogItemDomainRepository.partialUpdate(catalogItemDomain);
    }

    /**
     * Get all the catalogItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<CatalogItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogItems");
        return catalogItemDomainRepository.findAll(pageable);
    }

    /**
     * Get one catalogItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<CatalogItemDTO> findOne(Long id) {
        log.debug("Request to get CatalogItem : {}", id);
        return catalogItemDomainRepository.findOne(id);
    }

    /**
     * Delete the catalogItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogItem : {}", id);
        catalogItemDomainRepository.delete(id);
    }
}
