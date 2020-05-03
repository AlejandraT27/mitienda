package com.menttech.mitienda.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.menttech.mitienda.domain.Seller} entity.
 */
public class SellerDTO implements Serializable {
    
    private Long id;

    @NotNull
    private String firstName;


    private Long userId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long stockId) {
        this.userId = stockId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SellerDTO sellerDTO = (SellerDTO) o;
        if (sellerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), sellerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SellerDTO{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", userId=" + getUserId() +
            "}";
    }
}
