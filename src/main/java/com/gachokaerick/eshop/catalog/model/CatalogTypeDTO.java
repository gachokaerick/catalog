package com.gachokaerick.eshop.catalog.model;

import io.swagger.annotations.ApiModel;
import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.gachokaerick.eshop.catalog.model.CatalogType} entity.
 */
@ApiModel(description = "@author Erick Gachoka")
public class CatalogTypeDTO implements Serializable {

    private Long id;

    @NotNull
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CatalogTypeDTO)) {
            return false;
        }

        CatalogTypeDTO catalogTypeDTO = (CatalogTypeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, catalogTypeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CatalogTypeDTO{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
