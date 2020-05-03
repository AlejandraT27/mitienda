package com.menttech.mitienda.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Stock.
 */
@Entity
@Table(name = "stock")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "number_product", nullable = false)
    private String numberProduct;

    @NotNull
    @Column(name = "name_product", nullable = false)
    private String nameProduct;

    @Column(name = "description")
    private String description;

    @Column(name = "inventory")
    private String inventory;

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

    public Stock numberProduct(String numberProduct) {
        this.numberProduct = numberProduct;
        return this;
    }

    public void setNumberProduct(String numberProduct) {
        this.numberProduct = numberProduct;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public Stock nameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
        return this;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getDescription() {
        return description;
    }

    public Stock description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInventory() {
        return inventory;
    }

    public Stock inventory(String inventory) {
        this.inventory = inventory;
        return this;
    }

    public void setInventory(String inventory) {
        this.inventory = inventory;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Stock)) {
            return false;
        }
        return id != null && id.equals(((Stock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Stock{" +
            "id=" + getId() +
            ", numberProduct='" + getNumberProduct() + "'" +
            ", nameProduct='" + getNameProduct() + "'" +
            ", description='" + getDescription() + "'" +
            ", inventory='" + getInventory() + "'" +
            "}";
    }
}
