package com.gachokaerick.eshop.catalog.model;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * @author Erick Gachoka
 */
@Entity
@Table(name = "catalog_brand")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class CatalogBrand implements Serializable {

    public CatalogBrand() {}

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "brand", nullable = false)
    private String brand;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CatalogBrand id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return this.brand;
    }

    public CatalogBrand brand(String brand) {
        this.setBrand(brand);
        return this;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogBrand)) {
            return false;
        }
        return id != null && id.equals(((CatalogBrand) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogBrand{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            "}";
    }
}
