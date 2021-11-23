package com.gachokaerick.eshop.catalog.domain;

import com.gachokaerick.eshop.catalog.exception.DomainException;
import com.gachokaerick.eshop.catalog.model.CatalogItemDTO;
import java.math.BigDecimal;

/**
 * @author Erick Gachoka
 */
public class CatalogItem {

    static final String domainName = "CatalogItem";

    private CatalogItemDTO catalogItemDTO;

    private CatalogItem(CatalogItemBuilder builder) {
        catalogItemDTO = builder.catalogItemDTO;
    }

    /**
     * Decrements the quantity of a particular item in inventory and ensures the restockThreshold hasn't
     * been breached. If so, a RestockRequest is generated in CheckThreshold.
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
            throw DomainException.throwDomainException(
                domainName,
                "Empty stock. " + "Product " + catalogItemDTO.getName() + " is sold out"
            );
        }
        if (quantityDesired <= 0) {
            throw DomainException.throwDomainException(domainName, "Item units desired should be greater than zero");
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

    public static class CatalogItemBuilder {

        private CatalogItemDTO catalogItemDTO;

        public CatalogItemBuilder() {}

        public CatalogItemBuilder withCatalogItemDTO(CatalogItemDTO catalogItemDTO) {
            this.catalogItemDTO = catalogItemDTO;
            return this;
        }

        public CatalogItem build() {
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
            if (this.catalogItemDTO.getCatalogBrand().getId() == null) {
                throw DomainException.throwDomainException(domainName, "catalog brand for this item must exist");
            }
            if (this.catalogItemDTO.getCatalogType().getId() == null) {
                throw DomainException.throwDomainException(domainName, "catalog type for this item must exist");
            }
            return new CatalogItem(this);
        }
    }
}
