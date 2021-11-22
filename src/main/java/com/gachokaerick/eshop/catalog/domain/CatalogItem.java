package com.gachokaerick.eshop.catalog.domain;

import com.gachokaerick.eshop.catalog.exception.DomainException;
import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.model.CatalogType;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;

public class CatalogItem {

    private String domainName = "CatalogItem";

    private Long id;

    @NotNull
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    private String pictureFileName;

    private String pictureUrl;

    @NotNull
    private Integer availableStock;

    /**
     * Available stock at which we should reorder
     */
    @NotNull
    private Integer restockThreshold;

    /**
     * Maximum number of units that can be in-stock at any time (due to physical/logistical constraints in warehouses)
     */
    @NotNull
    private Integer maxStockThreshold;

    private Boolean onReorder;

    @NotNull
    private CatalogBrand catalogBrand;

    @NotNull
    private CatalogType catalogType;

    /**
     * Decrements the quantity of a particular item in inventory and ensures the restockThreshold hasn't
     * been breached. If so, a RestockRequest is generated in CheckThreshold.
     * If there is sufficient stock of an item, then the integer returned at the end of this call should be the same as quantityDesired.
     * In the event that there is not sufficient stock available, the method will remove whatever stock is available and return that quantity to the client.
     * In this case, it is the responsibility of the client to determine if the amount that is returned is the same as quantityDesired.
     * It is invalid to pass in a negative number.
     *
     * @param quantityDesired quantity to deduct from stock
     * @return the number actually removed from stock
     */
    public int removeStock(int quantityDesired) {
        if (availableStock == 0) {
            throw DomainException.throwDomainException(domainName, "Empty stock. Product " + name + " is sold out");
        }
        if (quantityDesired <= 0) {
            throw DomainException.throwDomainException(domainName, "Item units desired should be greater than zero");
        }

        int removed = Math.min(quantityDesired, availableStock);
        this.availableStock -= removed;
        return removed;
    }
}
