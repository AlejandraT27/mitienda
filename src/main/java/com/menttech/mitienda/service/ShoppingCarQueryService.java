package com.menttech.mitienda.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.menttech.mitienda.domain.ShoppingCar;
import com.menttech.mitienda.domain.*; // for static metamodels
import com.menttech.mitienda.repository.ShoppingCarRepository;
import com.menttech.mitienda.service.dto.ShoppingCarCriteria;
import com.menttech.mitienda.service.dto.ShoppingCarDTO;
import com.menttech.mitienda.service.mapper.ShoppingCarMapper;

/**
 * Service for executing complex queries for {@link ShoppingCar} entities in the database.
 * The main input is a {@link ShoppingCarCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ShoppingCarDTO} or a {@link Page} of {@link ShoppingCarDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ShoppingCarQueryService extends QueryService<ShoppingCar> {

    private final Logger log = LoggerFactory.getLogger(ShoppingCarQueryService.class);

    private final ShoppingCarRepository shoppingCarRepository;

    private final ShoppingCarMapper shoppingCarMapper;

    public ShoppingCarQueryService(ShoppingCarRepository shoppingCarRepository, ShoppingCarMapper shoppingCarMapper) {
        this.shoppingCarRepository = shoppingCarRepository;
        this.shoppingCarMapper = shoppingCarMapper;
    }

    /**
     * Return a {@link List} of {@link ShoppingCarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ShoppingCarDTO> findByCriteria(ShoppingCarCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ShoppingCar> specification = createSpecification(criteria);
        return shoppingCarMapper.toDto(shoppingCarRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ShoppingCarDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ShoppingCarDTO> findByCriteria(ShoppingCarCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ShoppingCar> specification = createSpecification(criteria);
        return shoppingCarRepository.findAll(specification, page)
            .map(shoppingCarMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ShoppingCarCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ShoppingCar> specification = createSpecification(criteria);
        return shoppingCarRepository.count(specification);
    }

    /**
     * Function to convert {@link ShoppingCarCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ShoppingCar> createSpecification(ShoppingCarCriteria criteria) {
        Specification<ShoppingCar> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ShoppingCar_.id));
            }
            if (criteria.getNumberProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumberProduct(), ShoppingCar_.numberProduct));
            }
            if (criteria.getProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProduct(), ShoppingCar_.product));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ShoppingCar_.description));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), ShoppingCar_.quantity));
            }
            if (criteria.getTotalPurchase() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPurchase(), ShoppingCar_.totalPurchase));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(ShoppingCar_.user, JoinType.LEFT).get(Customer_.id)));
            }
        }
        return specification;
    }
}
