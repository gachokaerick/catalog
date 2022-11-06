package com.gachokaerick.eshop.catalog.service.dto;

import com.gachokaerick.eshop.catalog.domain.catalogItem.CatalogItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link CatalogItem} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class CatalogItemDTO implements Serializable {

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
    @ApiModelProperty(value = "Available stock at which we should reorder", required = true)
    private Integer restockThreshold;

    /**
     * Maximum number of units that can be in-stock at any time (due to physical/logistical constraints in warehouses)
     */
    @NotNull
    @ApiModelProperty(
        value = "Maximum number of units that can be in-stock at any time (due to physical/logistical constraints in warehouses)",
        required = true
    )
    private Integer maxStockThreshold;

    private Boolean onReorder;

    private CatalogBrandDTO catalogBrand;

    private CatalogTypeDTO catalogType;

    public CatalogItemDTO() {}

    public CatalogItemDTO(
        Long id,
        String name,
        String description,
        BigDecimal price,
        String pictureFileName,
        String pictureUrl,
        Integer availableStock,
        Integer restockThreshold,
        Integer maxStockThreshold,
        Boolean onReorder,
        CatalogBrandDTO catalogBrand,
        CatalogTypeDTO catalogType
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.pictureFileName = pictureFileName;
        this.pictureUrl = pictureUrl;
        this.availableStock = availableStock;
        this.restockThreshold = restockThreshold;
        this.maxStockThreshold = maxStockThreshold;
        this.onReorder = onReorder;
        this.catalogBrand = catalogBrand;
        this.catalogType = catalogType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CatalogItemDTO name(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CatalogItemDTO description(String description) {
        this.description = description;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public CatalogItemDTO price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getPictureFileName() {
        return pictureFileName;
    }

    public void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public CatalogItemDTO pictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
        return this;
    }

    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public CatalogItemDTO pictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
        return this;
    }

    public Integer getAvailableStock() {
        return availableStock;
    }

    public void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public CatalogItemDTO availableStock(Integer availableStock) {
        this.availableStock = availableStock;
        return this;
    }

    public Integer getRestockThreshold() {
        return restockThreshold;
    }

    public void setRestockThreshold(Integer restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    public CatalogItemDTO restockThreshold(Integer restockThreshold) {
        this.restockThreshold = restockThreshold;
        return this;
    }

    public Integer getMaxStockThreshold() {
        return maxStockThreshold;
    }

    public void setMaxStockThreshold(Integer maxStockThreshold) {
        this.maxStockThreshold = maxStockThreshold;
    }

    public CatalogItemDTO maxStockThreshold(Integer maxStockThreshold) {
        this.maxStockThreshold = maxStockThreshold;
        return this;
    }

    public Boolean getOnReorder() {
        return onReorder;
    }

    public void setOnReorder(Boolean onReorder) {
        this.onReorder = onReorder;
    }

    public CatalogItemDTO onReorder(Boolean onReorder) {
        this.onReorder = onReorder;
        return this;
    }

    public CatalogBrandDTO getCatalogBrand() {
        return catalogBrand;
    }

    public void setCatalogBrand(CatalogBrandDTO catalogBrand) {
        this.catalogBrand = catalogBrand;
    }

    public CatalogItemDTO catalogBrand(CatalogBrandDTO catalogBrand) {
        this.catalogBrand = catalogBrand;
        return this;
    }

    public CatalogTypeDTO getCatalogType() {
        return catalogType;
    }

    public void setCatalogType(CatalogTypeDTO catalogType) {
        this.catalogType = catalogType;
    }

    public CatalogItemDTO catalogType(CatalogTypeDTO catalogType) {
        this.catalogType = catalogType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogItemDTO)) {
            return false;
        }

        CatalogItemDTO catalogItemDTO = (CatalogItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogItemDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", price=" + getPrice() +
            ", pictureFileName='" + getPictureFileName() + "'" +
            ", pictureUrl='" + getPictureUrl() + "'" +
            ", availableStock=" + getAvailableStock() +
            ", restockThreshold=" + getRestockThreshold() +
            ", maxStockThreshold=" + getMaxStockThreshold() +
            ", onReorder='" + getOnReorder() + "'" +
            ", catalogBrand=" + getCatalogBrand() +
            ", catalogType=" + getCatalogType() +
            "}";
    }
}
