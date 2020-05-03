package com.menttech.mitienda.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.math.BigDecimal;

/**
 * A ShoppingCar.
 */
@Entity
@Table(name = "shopping_car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ShoppingCar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "number_product", nullable = false)
    private String numberProduct;

    @NotNull
    @Column(name = "product", nullable = false)
    private String product;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @Column(name = "total_purchase", precision = 21, scale = 2)
    private BigDecimal totalPurchase;

    @OneToOne
    @JoinColumn(unique = true)
    private Customer user;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumberProduct() {
        return numberProduct;
    }

    public ShoppingCar numberProduct(String numberProduct) {
        this.numberProduct = numberProduct;
        return this;
    }

    public void setNumberProduct(String numberProduct) {
        this.numberProduct = numberProduct;
    }

    public String getProduct() {
        return product;
    }

    public ShoppingCar product(String product) {
        this.product = product;
        return this;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescription() {
        return description;
    }

    public ShoppingCar description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ShoppingCar quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPurchase() {
        return totalPurchase;
    }

    public ShoppingCar totalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
        return this;
    }

    public void setTotalPurchase(BigDecimal totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public Customer getUser() {
        return user;
    }

    public ShoppingCar user(Customer customer) {
        this.user = customer;
        return this;
    }

    public void setUser(Customer customer) {
        this.user = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShoppingCar)) {
            return false;
        }
        return id != null && id.equals(((ShoppingCar) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShoppingCar{" +
            "id=" + getId() +
            ", numberProduct='" + getNumberProduct() + "'" +
            ", product='" + getProduct() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", totalPurchase=" + getTotalPurchase() +
            "}";
    }
}
