package com.menttech.mitienda.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.menttech.mitienda.domain.ShoppingCar} entity. This class is used
 * in {@link com.menttech.mitienda.web.rest.ShoppingCarResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /shopping-cars?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ShoppingCarCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter numberProduct;

    private StringFilter product;

    private StringFilter description;

    private BigDecimalFilter quantity;

    private BigDecimalFilter totalPurchase;

    private LongFilter userId;

    public ShoppingCarCriteria() {
    }

    public ShoppingCarCriteria(ShoppingCarCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.numberProduct = other.numberProduct == null ? null : other.numberProduct.copy();
        this.product = other.product == null ? null : other.product.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.quantity = other.quantity == null ? null : other.quantity.copy();
        this.totalPurchase = other.totalPurchase == null ? null : other.totalPurchase.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
    }

    @Override
    public ShoppingCarCriteria copy() {
        return new ShoppingCarCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getNumberProduct() {
        return numberProduct;
    }

    public void setNumberProduct(StringFilter numberProduct) {
        this.numberProduct = numberProduct;
    }

    public StringFilter getProduct() {
        return product;
    }

    public void setProduct(StringFilter product) {
        this.product = product;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimalFilter quantity) {
        this.quantity = quantity;
    }

    public BigDecimalFilter getTotalPurchase() {
        return totalPurchase;
    }

    public void setTotalPurchase(BigDecimalFilter totalPurchase) {
        this.totalPurchase = totalPurchase;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ShoppingCarCriteria that = (ShoppingCarCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(numberProduct, that.numberProduct) &&
            Objects.equals(product, that.product) &&
            Objects.equals(description, that.description) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(totalPurchase, that.totalPurchase) &&
            Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        numberProduct,
        product,
        description,
        quantity,
        totalPurchase,
        userId
        );
    }

    @Override
    public String toString() {
        return "ShoppingCarCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (numberProduct != null ? "numberProduct=" + numberProduct + ", " : "") +
                (product != null ? "product=" + product + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (quantity != null ? "quantity=" + quantity + ", " : "") +
                (totalPurchase != null ? "totalPurchase=" + totalPurchase + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
            "}";
    }

}
