package com.menttech.mitienda.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.menttech.mitienda.domain.ProductCategory} entity.
 */
public class ProductCategoryDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String name;

    private String description;


    private Long productoId;

    private String productoNombre;
    
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProductoId() {
        return productoId;
    }

    public void setProductoId(Long productId) {
        this.productoId = productId;
    }

    public String getProductoNombre() {
        return productoNombre;
    }

    public void setProductoNombre(String productNombre) {
        this.productoNombre = productNombre;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductCategoryDTO productCategoryDTO = (ProductCategoryDTO) o;
        if (productCategoryDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productCategoryDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductCategoryDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", productoId=" + getProductoId() +
            ", productoNombre='" + getProductoNombre() + "'" +
            "}";
    }
}
