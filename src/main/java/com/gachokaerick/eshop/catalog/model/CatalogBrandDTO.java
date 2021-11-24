package com.gachokaerick.eshop.catalog.model;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gachokaerick.eshop.catalog.model.CatalogBrand} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class CatalogBrandDTO implements Serializable {

    private Long id;

    @NotNull
    private String brand;

    public CatalogBrandDTO() {}

    public CatalogBrandDTO(Long id, String brand) {
        this.id = id;
        this.brand = brand;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogBrandDTO)) {
            return false;
        }

        CatalogBrandDTO catalogBrandDTO = (CatalogBrandDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogBrandDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogBrandDTO{" +
            "id=" + getId() +
            ", brand='" + getBrand() + "'" +
            "}";
    }
}
