package com.menttech.mitienda.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.menttech.mitienda.domain.enumeration.Size;
import com.menttech.mitienda.domain.enumeration.Color;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.BigDecimalFilter;

/**
 * Criteria class for the {@link com.menttech.mitienda.domain.Product} entity. This class is used
 * in {@link com.menttech.mitienda.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ProductCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Size
     */
    public static class SizeFilter extends Filter<Size> {

        public SizeFilter() {
        }

        public SizeFilter(SizeFilter filter) {
            super(filter);
        }

        @Override
        public SizeFilter copy() {
            return new SizeFilter(this);
        }

    }
    /**
     * Class for filtering Color
     */
    public static class ColorFilter extends Filter<Color> {

        public ColorFilter() {
        }

        public ColorFilter(ColorFilter filter) {
            super(filter);
        }

        @Override
        public ColorFilter copy() {
            return new ColorFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter description;

    private BigDecimalFilter purchasePrice;

    private BigDecimalFilter salePrice;

    private SizeFilter size;

    private ColorFilter color;

    private StringFilter stock;

    private LongFilter userId;

    private LongFilter productCategoyId;

    private LongFilter productCategoryId;

    public ProductCriteria() {
    }

    public ProductCriteria(ProductCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.description = other.description == null ? null : other.description.copy();
        this.purchasePrice = other.purchasePrice == null ? null : other.purchasePrice.copy();
        this.salePrice = other.salePrice == null ? null : other.salePrice.copy();
        this.size = other.size == null ? null : other.size.copy();
        this.color = other.color == null ? null : other.color.copy();
        this.stock = other.stock == null ? null : other.stock.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.productCategoyId = other.productCategoyId == null ? null : other.productCategoyId.copy();
        this.productCategoryId = other.productCategoryId == null ? null : other.productCategoryId.copy();
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public BigDecimalFilter getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(BigDecimalFilter purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public BigDecimalFilter getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(BigDecimalFilter salePrice) {
        this.salePrice = salePrice;
    }

    public SizeFilter getSize() {
        return size;
    }

    public void setSize(SizeFilter size) {
        this.size = size;
    }

    public ColorFilter getColor() {
        return color;
    }

    public void setColor(ColorFilter color) {
        this.color = color;
    }

    public StringFilter getStock() {
        return stock;
    }

    public void setStock(StringFilter stock) {
        this.stock = stock;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getProductCategoyId() {
        return productCategoyId;
    }

    public void setProductCategoyId(LongFilter productCategoyId) {
        this.productCategoyId = productCategoyId;
    }

    public LongFilter getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(LongFilter productCategoryId) {
        this.productCategoryId = productCategoryId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(purchasePrice, that.purchasePrice) &&
            Objects.equals(salePrice, that.salePrice) &&
            Objects.equals(size, that.size) &&
            Objects.equals(color, that.color) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(productCategoyId, that.productCategoyId) &&
            Objects.equals(productCategoryId, that.productCategoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        description,
        purchasePrice,
        salePrice,
        size,
        color,
        stock,
        userId,
        productCategoyId,
        productCategoryId
        );
    }

    @Override
    public String toString() {
        return "ProductCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (description != null ? "description=" + description + ", " : "") +
                (purchasePrice != null ? "purchasePrice=" + purchasePrice + ", " : "") +
                (salePrice != null ? "salePrice=" + salePrice + ", " : "") +
                (size != null ? "size=" + size + ", " : "") +
                (color != null ? "color=" + color + ", " : "") +
                (stock != null ? "stock=" + stock + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (productCategoyId != null ? "productCategoyId=" + productCategoyId + ", " : "") +
                (productCategoryId != null ? "productCategoryId=" + productCategoryId + ", " : "") +
            "}";
    }

}
