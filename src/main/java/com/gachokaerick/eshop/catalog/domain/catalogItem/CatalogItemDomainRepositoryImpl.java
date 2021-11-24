package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.web.rest.errors.BadRequestAlertException;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class CatalogItemDomainRepositoryImpl implements CatalogItemDomainRepository {

    private final CatalogItemRepository catalogItemRepository;
    private final CatalogItemMapper catalogItemMapper;
    private static final String ENTITY_NAME = "CatalogItem";

    public CatalogItemDomainRepositoryImpl(CatalogItemRepository catalogItemRepository, CatalogItemMapper catalogItemMapper) {
        this.catalogItemRepository = catalogItemRepository;
        this.catalogItemMapper = catalogItemMapper;
    }

    @Override
    public CatalogItemDTO create(CatalogItemDomain catalogItemDomain) {
        if (catalogItemDomain.getCatalogItemDTO().getId() != null) {
            throw new BadRequestAlertException("A new catalogItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CatalogItem catalogItem = catalogItemMapper.toEntity(catalogItemDomain.getCatalogItemDTO());
        catalogItem = catalogItemRepository.save(catalogItem);
        return catalogItemMapper.toDto(catalogItem);
    }

    @Override
    public CatalogItemDTO update(CatalogItemDomain catalogItemDomain) {
        if (catalogItemDomain.getCatalogItemDTO().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!catalogItemRepository.existsById(catalogItemDomain.getCatalogItemDTO().getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CatalogItem catalogItem = catalogItemMapper.toEntity(catalogItemDomain.getCatalogItemDTO());
        catalogItem = catalogItemRepository.save(catalogItem);
        return catalogItemMapper.toDto(catalogItem);
    }

    @Override
    public Optional<CatalogItemDTO> partialUpdate(CatalogItemDomain catalogItemDomain) {
        if (catalogItemDomain.getCatalogItemDTO().getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }

        if (!catalogItemRepository.existsById(catalogItemDomain.getCatalogItemDTO().getId())) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        return catalogItemRepository
            .findById(catalogItemDomain.getCatalogItemDTO().getId())
            .map(existingCatalogItem -> {
                catalogItemMapper.partialUpdate(existingCatalogItem, catalogItemDomain.getCatalogItemDTO());
                return existingCatalogItem;
            })
            .map(catalogItemRepository::save)
            .map(catalogItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CatalogItemDTO> findAll(Pageable pageable) {
        return catalogItemRepository.findAll(pageable).map(catalogItemMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CatalogItemDTO> findOne(Long id) {
        return catalogItemRepository.findById(id).map(catalogItemMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        catalogItemRepository.deleteById(id);
    }
}
