package com.gachokaerick.eshop.catalog.service;

import com.gachokaerick.eshop.catalog.model.CatalogType;
import com.gachokaerick.eshop.catalog.model.CatalogTypeDTO;
import com.gachokaerick.eshop.catalog.model.CatalogTypeMapper;
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

    private final CatalogTypeMapper catalogTypeMapper;

    public CatalogTypeService(CatalogTypeRepository catalogTypeRepository, CatalogTypeMapper catalogTypeMapper) {
        this.catalogTypeRepository = catalogTypeRepository;
        this.catalogTypeMapper = catalogTypeMapper;
    }

    /**
     * Save a catalogType.
     *
     * @param catalogTypeDTO the entity to save.
     * @return the persisted entity.
     */
    public CatalogTypeDTO save(CatalogTypeDTO catalogTypeDTO) {
        log.debug("Request to save CatalogType : {}", catalogTypeDTO);
        CatalogType catalogType = catalogTypeMapper.toEntity(catalogTypeDTO);
        catalogType = catalogTypeRepository.save(catalogType);
        return catalogTypeMapper.toDto(catalogType);
    }

    /**
     * Partially update a catalogType.
     *
     * @param catalogTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CatalogTypeDTO> partialUpdate(CatalogTypeDTO catalogTypeDTO) {
        log.debug("Request to partially update CatalogType : {}", catalogTypeDTO);

        return catalogTypeRepository
            .findById(catalogTypeDTO.getId())
            .map(existingCatalogType -> {
                catalogTypeMapper.partialUpdate(existingCatalogType, catalogTypeDTO);

                return existingCatalogType;
            })
            .map(catalogTypeRepository::save)
            .map(catalogTypeMapper::toDto);
    }

    /**
     * Get all the catalogTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CatalogTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CatalogTypes");
        return catalogTypeRepository.findAll(pageable).map(catalogTypeMapper::toDto);
    }

    /**
     * Get one catalogType by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CatalogTypeDTO> findOne(Long id) {
        log.debug("Request to get CatalogType : {}", id);
        return catalogTypeRepository.findById(id).map(catalogTypeMapper::toDto);
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
