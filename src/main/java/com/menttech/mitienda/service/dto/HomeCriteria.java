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

/**
 * Criteria class for the {@link com.menttech.mitienda.domain.Home} entity. This class is used
 * in {@link com.menttech.mitienda.web.rest.HomeResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /homes?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class HomeCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter firstName;

    private StringFilter lastName;

    private StringFilter adress;

    private StringFilter phone;

    private StringFilter locality;

    public HomeCriteria() {
    }

    public HomeCriteria(HomeCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.firstName = other.firstName == null ? null : other.firstName.copy();
        this.lastName = other.lastName == null ? null : other.lastName.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.locality = other.locality == null ? null : other.locality.copy();
    }

    @Override
    public HomeCriteria copy() {
        return new HomeCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public StringFilter getAdress() {
        return adress;
    }

    public void setAdress(StringFilter adress) {
        this.adress = adress;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getLocality() {
        return locality;
    }

    public void setLocality(StringFilter locality) {
        this.locality = locality;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HomeCriteria that = (HomeCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(locality, that.locality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        firstName,
        lastName,
        adress,
        phone,
        locality
        );
    }

    @Override
    public String toString() {
        return "HomeCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (adress != null ? "adress=" + adress + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (locality != null ? "locality=" + locality + ", " : "") +
            "}";
    }

}
