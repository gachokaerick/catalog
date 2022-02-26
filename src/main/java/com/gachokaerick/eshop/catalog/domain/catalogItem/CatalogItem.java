package com.gachokaerick.eshop.catalog.domain.catalogItem;

import com.gachokaerick.eshop.catalog.model.CatalogBrand;
import com.gachokaerick.eshop.catalog.model.CatalogType;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "catalog_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatalogItem implements Serializable {

    // remains public for testUtil equals verifier access
    public CatalogItem() {}

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "picture_file_name")
    private String pictureFileName;

    @Column(name = "picture_url")
    private String pictureUrl;

    @NotNull
    @Column(name = "available_stock", nullable = false)
    private Integer availableStock;

    /**
     * Available stock at which we should reorder
     */
    @NotNull
    @Column(name = "restock_threshold", nullable = false)
    private Integer restockThreshold;

    /**
     * Maximum number of units that can be in-stock at any time (due to physical/logistical constraints in warehouses)
     */
    @NotNull
    @Column(name = "max_stock_threshold", nullable = false)
    private Integer maxStockThreshold;

    @Column(name = "on_reorder")
    private Boolean onReorder;

    @ManyToOne(optional = false)
    @NotNull
    private CatalogBrand catalogBrand;

    @ManyToOne(optional = false)
    @NotNull
    private CatalogType catalogType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    CatalogItem id(Long id) {
        this.setId(id);
        return this;
    }

    void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    CatalogItem name(String name) {
        this.setName(name);
        return this;
    }

    void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    CatalogItem description(String description) {
        this.setDescription(description);
        return this;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    CatalogItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPictureFileName() {
        return this.pictureFileName;
    }

    CatalogItem pictureFileName(String pictureFileName) {
        this.setPictureFileName(pictureFileName);
        return this;
    }

    void setPictureFileName(String pictureFileName) {
        this.pictureFileName = pictureFileName;
    }

    public String getPictureUrl() {
        return this.pictureUrl;
    }

    CatalogItem pictureUrl(String pictureUrl) {
        this.setPictureUrl(pictureUrl);
        return this;
    }

    void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public Integer getAvailableStock() {
        return this.availableStock;
    }

    CatalogItem availableStock(Integer availableStock) {
        this.setAvailableStock(availableStock);
        return this;
    }

    void setAvailableStock(Integer availableStock) {
        this.availableStock = availableStock;
    }

    public Integer getRestockThreshold() {
        return this.restockThreshold;
    }

    CatalogItem restockThreshold(Integer restockThreshold) {
        this.setRestockThreshold(restockThreshold);
        return this;
    }

    void setRestockThreshold(Integer restockThreshold) {
        this.restockThreshold = restockThreshold;
    }

    public Integer getMaxStockThreshold() {
        return this.maxStockThreshold;
    }

    CatalogItem maxStockThreshold(Integer maxStockThreshold) {
        this.setMaxStockThreshold(maxStockThreshold);
        return this;
    }

    void setMaxStockThreshold(Integer maxStockThreshold) {
        this.maxStockThreshold = maxStockThreshold;
    }

    public Boolean getOnReorder() {
        return this.onReorder;
    }

    CatalogItem onReorder(Boolean onReorder) {
        this.setOnReorder(onReorder);
        return this;
    }

    void setOnReorder(Boolean onReorder) {
        this.onReorder = onReorder;
    }

    public CatalogBrand getCatalogBrand() {
        return this.catalogBrand;
    }

    void setCatalogBrand(CatalogBrand catalogBrand) {
        this.catalogBrand = catalogBrand;
    }

    CatalogItem catalogBrand(CatalogBrand catalogBrand) {
        this.setCatalogBrand(catalogBrand);
        return this;
    }

    public CatalogType getCatalogType() {
        return this.catalogType;
    }

    void setCatalogType(CatalogType catalogType) {
        this.catalogType = catalogType;
    }

    CatalogItem catalogType(CatalogType catalogType) {
        this.setCatalogType(catalogType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogItem)) {
            return false;
        }
        return id != null && id.equals(((CatalogItem) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogItem{" +
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
            "}";
    }
}
