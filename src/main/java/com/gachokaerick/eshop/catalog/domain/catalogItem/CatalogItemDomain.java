package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.exception.DomainException;
import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.model.CatalogType;
import com.gachokaerick.eshop.catalog.service.dto.CatalogItemDTO;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapper;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogBrandMapperImpl;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogTypeMapper;
import com.gachokaerick.eshop.catalog.service.mapper.CatalogTypeMapperImpl;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

/**
 * @author Erick Gachoka
 */
public class CatalogItemDomain {

    static final String domainName = "CatalogItem";

    private final CatalogItemDTO catalogItemDTO;

    private CatalogItemDomain(CatalogItemBuilder builder) {
        catalogItemDTO = builder.catalogItemDTO;
    }

    /**
     * Decrements the quantity of a particular item in inventory and ensures the restockThreshold hasn't
     * been breached.
     * If there is sufficient stock of an item, then the integer returned at the end of this call should be the same as quantityDesired.
     * In the event that there is no sufficient stock available, the method will remove whatever stock is available and return that quantity to the client.
     * In this case, it is the responsibility of the client to determine if the amount that is returned is the same as quantityDesired.
     * It is invalid to pass in a negative number.
     *
     * @param quantityDesired quantity to deduct from stock
     * @return the number actually removed from stock
     */
    public int removeStock(int quantityDesired) {
        if (catalogItemDTO.getAvailableStock() == 0) {
            throw DomainException.throwDomainException(domainName, "Empty stock. Product " + catalogItemDTO.getName() + " is sold out");
        }
        if (quantityDesired <= 0) {
            throw DomainException.throwDomainException(domainName, "Units to remove should be greater than zero");
        }

        int removed = Math.min(quantityDesired, catalogItemDTO.getAvailableStock());
        catalogItemDTO.setAvailableStock(catalogItemDTO.getAvailableStock() - removed);
        return removed;
    }

    /**
     * Increments the quantity of a particular item in inventory.
     *
     * @param quantity amount to add to stock
     * @return the quantity that has been added to stock
     */
    public int addStock(int quantity) {
        int original = catalogItemDTO.getAvailableStock();
        // The quantity that the client is trying to add to stock is greater than what can be physically accommodated in the Warehouse
        if ((catalogItemDTO.getAvailableStock() + quantity) > catalogItemDTO.getMaxStockThreshold()) {
            catalogItemDTO.setAvailableStock(catalogItemDTO.getMaxStockThreshold());
        } else {
            catalogItemDTO.setAvailableStock(catalogItemDTO.getAvailableStock() + quantity);
        }

        catalogItemDTO.setOnReorder(false);
        return catalogItemDTO.getAvailableStock() - original;
    }

    public CatalogItem getCatalogItem() {
        CatalogItem catalogItem = new CatalogItem();
        CatalogBrandMapper catalogBrandMapper = new CatalogBrandMapperImpl();
        CatalogTypeMapper catalogTypeMapper = new CatalogTypeMapperImpl();

        catalogItem.setId(catalogItemDTO.getId());
        catalogItem.setName(catalogItemDTO.getName());
        catalogItem.setDescription(catalogItemDTO.getDescription());
        catalogItem.setPrice(catalogItemDTO.getPrice());
        catalogItem.setPictureFileName(catalogItemDTO.getPictureFileName());
        catalogItem.setPictureUrl(catalogItemDTO.getPictureUrl());
        catalogItem.setAvailableStock(catalogItemDTO.getAvailableStock());
        catalogItem.setRestockThreshold(catalogItemDTO.getRestockThreshold());
        catalogItem.setMaxStockThreshold(catalogItemDTO.getMaxStockThreshold());
        catalogItem.setOnReorder(catalogItemDTO.getOnReorder());
        catalogItem.setCatalogBrand(catalogBrandMapper.toEntity(catalogItemDTO.getCatalogBrand()));
        catalogItem.setCatalogType(catalogTypeMapper.toEntity(catalogItemDTO.getCatalogType()));
        return catalogItem;
    }

    public CatalogItem setBrand(CatalogItem catalogItem, CatalogBrand catalogBrand) {
        if (catalogBrand.getId() == null) {
            throw DomainException.throwDomainException(domainName, "Catalog brand id cannot be null");
        }
        catalogItem.setCatalogBrand(catalogBrand);
        return catalogItem;
    }

    public CatalogItem setType(CatalogItem catalogItem, CatalogType catalogType) {
        if (catalogType.getId() == null) {
            throw DomainException.throwDomainException(domainName, "Catalog brand id cannot be null");
        }
        catalogItem.setCatalogType(catalogType);
        return catalogItem;
    }

    public CatalogItemDTO getCatalogItemDTO() {
        return catalogItemDTO;
    }

    public static class CatalogItemBuilder {

        private CatalogItemDTO catalogItemDTO;

        public CatalogItemBuilder() {}

        public CatalogItemBuilder withCatalogItemDTO(@NotNull CatalogItemDTO catalogItemDTO) {
            this.catalogItemDTO = catalogItemDTO;
            return this;
        }

        private boolean isAcceptable() {
            if (this.catalogItemDTO == null) {
                throw DomainException.throwDomainException(domainName, "CatalogItemDTO cannot be null");
            }
            if (
                this.catalogItemDTO.getName() == null ||
                this.catalogItemDTO.getPrice() == null ||
                this.catalogItemDTO.getAvailableStock() == null ||
                this.catalogItemDTO.getRestockThreshold() == null ||
                this.catalogItemDTO.getMaxStockThreshold() == null ||
                this.catalogItemDTO.getCatalogBrand() == null ||
                this.catalogItemDTO.getCatalogType() == null
            ) {
                throw DomainException.throwDomainException(
                    domainName,
                    "name, price, availableStock, restockThreshold, " + "maxStockThreshold, catalogBrand and catalogType cannot be null"
                );
            }
            if (this.catalogItemDTO.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                throw DomainException.throwDomainException(domainName, "price should be greater than zero");
            }
            if (this.catalogItemDTO.getAvailableStock() < 0) {
                throw DomainException.throwDomainException(domainName, "available stock should be greater than zero");
            }
            if (this.catalogItemDTO.getRestockThreshold() < 0) {
                throw DomainException.throwDomainException(domainName, "restock threshold should be greater than zero");
            }
            if (this.catalogItemDTO.getMaxStockThreshold() <= 0) {
                throw DomainException.throwDomainException(domainName, "max stock threshold should be greater than zero");
            }
            if (this.catalogItemDTO.getAvailableStock() > this.catalogItemDTO.getMaxStockThreshold()) {
                throw DomainException.throwDomainException(domainName, "available stock should not exceed maximum stock threshold");
            }
            if (this.catalogItemDTO.getRestockThreshold() > this.catalogItemDTO.getMaxStockThreshold()) {
                throw DomainException.throwDomainException(domainName, "restockThreshold should not exceed maximum stock threshold");
            }
            if (this.catalogItemDTO.getCatalogBrand().getId() == null || this.catalogItemDTO.getCatalogBrand().getBrand() == null) {
                throw DomainException.throwDomainException(domainName, "catalog brand for this item must exist");
            }
            if (this.catalogItemDTO.getCatalogType().getId() == null || this.catalogItemDTO.getCatalogType().getType() == null) {
                throw DomainException.throwDomainException(domainName, "catalog type for this item must exist");
            }
            return true;
        }

        public CatalogItemDomain build() {
            if (isAcceptable()) {
                return new CatalogItemDomain(this);
            }
            throw DomainException.throwDomainException(domainName, "cannot build a catalog with invalid data");
        }
    }
}
