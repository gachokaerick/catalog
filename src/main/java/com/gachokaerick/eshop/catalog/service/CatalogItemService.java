package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemDomain;
import com.gachokaerick.eshop.catalog.repository.CatalogItemRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogItemMapper;
import com.gachokaerick.eshop.catalog.web.rest.errors.BadRequestAlertException;
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

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogItemMapper catalogItemMapper;

    public CatalogItemService(CatalogItemRepository catalogItemRepository, CatalogItemMapper catalogItemMapper) {
        this.catalogItemRepository = catalogItemRepository;
        this.catalogItemMapper = catalogItemMapper;
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

        CatalogItem catalogItem = catalogItemDomain.getCatalogItem();
        catalogItem = catalogItemRepository.save(catalogItem);
        return catalogItemMapper.toDto(catalogItem);
    }

    public CatalogItemDTO update(CatalogItemDTO catalogItemDTO) {
        log.debug("Request to update a CatalogItem : {}", catalogItemDTO);
        CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItemDTO).build();

        CatalogItem catalogItem = catalogItemDomain.getCatalogItem();
        catalogItem = catalogItemRepository.save(catalogItem);
        return catalogItemMapper.toDto(catalogItem);
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

        return catalogItemRepository
            .findById(catalogItemDomain.getCatalogItemDTO().getId())
            .map(existingCatalogItem -> {
                catalogItemMapper.partialUpdate(existingCatalogItem, catalogItemDomain.getCatalogItemDTO());
                return existingCatalogItem;
            })
            .map(catalogItemRepository::save)
            .map(catalogItemMapper::toDto);
    }

    /**
     * Get all the catalogItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    public Page<CatalogItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogItems");
        return catalogItemRepository.findAll(pageable).map(catalogItemMapper::toDto);
    }

    /**
     * Get one catalogItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */

    public Optional<CatalogItemDTO> findOne(Long id) {
        log.debug("Request to get CatalogItem : {}", id);
        return catalogItemRepository.findById(id).map(catalogItemMapper::toDto);
    }

    /**
     * Delete the catalogItem by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CatalogItem : {}", id);
        catalogItemRepository.deleteById(id);
    }
}
