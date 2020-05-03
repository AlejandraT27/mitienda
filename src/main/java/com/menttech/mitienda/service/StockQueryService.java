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

import com.menttech.mitienda.domain.Stock;
import com.menttech.mitienda.domain.*; // for static metamodels
import com.menttech.mitienda.repository.StockRepository;
import com.menttech.mitienda.service.dto.StockCriteria;
import com.menttech.mitienda.service.dto.StockDTO;
import com.menttech.mitienda.service.mapper.StockMapper;

/**
 * Service for executing complex queries for {@link Stock} entities in the database.
 * The main input is a {@link StockCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StockDTO} or a {@link Page} of {@link StockDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StockQueryService extends QueryService<Stock> {

    private final Logger log = LoggerFactory.getLogger(StockQueryService.class);

    private final StockRepository stockRepository;

    private final StockMapper stockMapper;

    public StockQueryService(StockRepository stockRepository, StockMapper stockMapper) {
        this.stockRepository = stockRepository;
        this.stockMapper = stockMapper;
    }

    /**
     * Return a {@link List} of {@link StockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StockDTO> findByCriteria(StockCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockMapper.toDto(stockRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StockDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StockDTO> findByCriteria(StockCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.findAll(specification, page)
            .map(stockMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StockCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Stock> specification = createSpecification(criteria);
        return stockRepository.count(specification);
    }

    /**
     * Function to convert {@link StockCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Stock> createSpecification(StockCriteria criteria) {
        Specification<Stock> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Stock_.id));
            }
            if (criteria.getNumberProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumberProduct(), Stock_.numberProduct));
            }
            if (criteria.getNameProduct() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNameProduct(), Stock_.nameProduct));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Stock_.description));
            }
            if (criteria.getInventory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInventory(), Stock_.inventory));
            }
        }
        return specification;
    }
}
