package com.menttech.mitienda.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.menttech.mitienda.domain.ShoppingCar} entity.
 */
public class ShoppingCarDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String numberProduct;

    @NotNull
    private String product;

    private String description;

    @NotNull
    private BigDecimal quantity;

    private BigDecimal totalPurchase;


    private Long userId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberProduct() {
        return numberProduct;
    }

    public void setNumberProduct(String numberProduct) {
        this.numberProduct = numberProduct;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long customerId) {
        this.userId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ShoppingCarDTO shoppingCarDTO = (ShoppingCarDTO) o;
        if (shoppingCarDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), shoppingCarDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ShoppingCarDTO{" +
            "id=" + getId() +
            ", numberProduct='" + getNumberProduct() + "'" +
            ", product='" + getProduct() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", totalPurchase=" + getTotalPurchase() +
            ", userId=" + getUserId() +
            "}";
    }
}
