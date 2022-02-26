package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemDomain;
import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItemMapper;
import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.model.CatalogType;
import com.gachokaerick.eshop.catalog.repository.CatalogBrandRepository;
import com.gachokaerick.eshop.catalog.repository.CatalogItemRepository;
import com.gachokaerick.eshop.catalog.repository.CatalogTypeRepository;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing CatalogItems.
 */
@Service
@Transactional
public class CatalogItemService {

    private final Logger log = LoggerFactory.getLogger(CatalogItemService.class);

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogItemMapper catalogItemMapper;
    private final CatalogBrandRepository catalogBrandRepository;
    private final CatalogTypeRepository catalogTypeRepository;

    public CatalogItemService(
        CatalogItemRepository catalogItemRepository,
        CatalogItemMapper catalogItemMapper,
        CatalogBrandRepository catalogBrandRepository,
        CatalogTypeRepository catalogTypeRepository
    ) {
        this.catalogItemRepository = catalogItemRepository;
        this.catalogItemMapper = catalogItemMapper;
        this.catalogBrandRepository = catalogBrandRepository;
        this.catalogTypeRepository = catalogTypeRepository;
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

        CatalogBrand brand = catalogBrandRepository.getById(catalogItemDTO.getCatalogBrand().getId());
        CatalogType type = catalogTypeRepository.getById(catalogItemDTO.getCatalogType().getId());

        catalogItem = catalogItemDomain.setBrand(catalogItem, brand);
        catalogItem = catalogItemDomain.setType(catalogItem, type);

        catalogItem = catalogItemRepository.save(catalogItem);
        return catalogItemMapper.toDto(catalogItem);
    }

    public CatalogItemDTO update(CatalogItemDTO catalogItemDTO) {
        log.debug("Request to update a CatalogItem : {}", catalogItemDTO);
        CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(catalogItemDTO).build();

        CatalogItem catalogItem = catalogItemDomain.getCatalogItem();

        if (catalogItemDTO.getCatalogBrand() != null) {
            CatalogBrand brand = catalogBrandRepository.getById(catalogItem.getCatalogBrand().getId());
            catalogItem = catalogItemDomain.setBrand(catalogItem, brand);
        }

        if (catalogItemDTO.getCatalogType() != null) {
            CatalogType type = catalogTypeRepository.getById(catalogItem.getCatalogType().getId());
            catalogItem = catalogItemDomain.setType(catalogItem, type);
        }

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

        return catalogItemRepository
            .findById(catalogItemDTO.getId())
            .map(existingCatalogItem -> {
                catalogItemMapper.partialUpdate(existingCatalogItem, catalogItemDTO);

                // ensure updates made are acceptable
                CatalogItemDTO dto = catalogItemMapper.toDto(existingCatalogItem);
                new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(dto).build();

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

    public Optional<CatalogItemDTO> partialUpdateAddStock(CatalogItemDTO catalogItemDTO, int quantity) {
        log.debug("Request to add stock to CatalogItem : {}, {}", quantity, catalogItemDTO);

        return catalogItemRepository
            .findById(catalogItemDTO.getId())
            .map(existingCatalogItem -> {
                CatalogItemDTO dto = catalogItemMapper.toDto(existingCatalogItem);

                CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(dto).build();
                catalogItemDomain.addStock(quantity);

                existingCatalogItem = catalogItemDomain.getCatalogItem();
                return existingCatalogItem;
            })
            .map(catalogItemRepository::save)
            .map(catalogItemMapper::toDto);
    }

    public Optional<CatalogItemDTO> partialUpdateRemoveStock(CatalogItemDTO catalogItemDTO, int quantity) {
        log.debug("Request to remove stock to CatalogItem : {}, {}", quantity, catalogItemDTO);

        return catalogItemRepository
            .findById(catalogItemDTO.getId())
            .map(existingCatalogItem -> {
                CatalogItemDTO dto = catalogItemMapper.toDto(existingCatalogItem);

                CatalogItemDomain catalogItemDomain = new CatalogItemDomain.CatalogItemBuilder().withCatalogItemDTO(dto).build();
                catalogItemDomain.removeStock(quantity);

                existingCatalogItem = catalogItemDomain.getCatalogItem();
                return existingCatalogItem;
            })
            .map(catalogItemRepository::save)
            .map(catalogItemMapper::toDto);
    }
}
