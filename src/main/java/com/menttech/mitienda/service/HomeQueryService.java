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

import com.menttech.mitienda.domain.Home;
import com.menttech.mitienda.domain.*; // for static metamodels
import com.menttech.mitienda.repository.HomeRepository;
import com.menttech.mitienda.service.dto.HomeCriteria;
import com.menttech.mitienda.service.dto.HomeDTO;
import com.menttech.mitienda.service.mapper.HomeMapper;

/**
 * Service for executing complex queries for {@link Home} entities in the database.
 * The main input is a {@link HomeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link HomeDTO} or a {@link Page} of {@link HomeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HomeQueryService extends QueryService<Home> {

    private final Logger log = LoggerFactory.getLogger(HomeQueryService.class);

    private final HomeRepository homeRepository;

    private final HomeMapper homeMapper;

    public HomeQueryService(HomeRepository homeRepository, HomeMapper homeMapper) {
        this.homeRepository = homeRepository;
        this.homeMapper = homeMapper;
    }

    /**
     * Return a {@link List} of {@link HomeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<HomeDTO> findByCriteria(HomeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Home> specification = createSpecification(criteria);
        return homeMapper.toDto(homeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link HomeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeDTO> findByCriteria(HomeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Home> specification = createSpecification(criteria);
        return homeRepository.findAll(specification, page)
            .map(homeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HomeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Home> specification = createSpecification(criteria);
        return homeRepository.count(specification);
    }

    /**
     * Function to convert {@link HomeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Home> createSpecification(HomeCriteria criteria) {
        Specification<Home> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Home_.id));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Home_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Home_.lastName));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), Home_.adress));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Home_.phone));
            }
            if (criteria.getLocality() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLocality(), Home_.locality));
            }
        }
        return specification;
    }
}
