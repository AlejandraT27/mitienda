package com.menttech.mitienda.service;

import com.menttech.mitienda.domain.Home;
import com.menttech.mitienda.repository.HomeRepository;
import com.menttech.mitienda.service.dto.HomeDTO;
import com.menttech.mitienda.service.mapper.HomeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Home}.
 */
@Service
@Transactional
public class HomeService {

    private final Logger log = LoggerFactory.getLogger(HomeService.class);

    private final HomeRepository homeRepository;

    private final HomeMapper homeMapper;

    public HomeService(HomeRepository homeRepository, HomeMapper homeMapper) {
        this.homeRepository = homeRepository;
        this.homeMapper = homeMapper;
    }

    /**
     * Save a home.
     *
     * @param homeDTO the entity to save.
     * @return the persisted entity.
     */
    public HomeDTO save(HomeDTO homeDTO) {
        log.debug("Request to save Home : {}", homeDTO);
        Home home = homeMapper.toEntity(homeDTO);
        home = homeRepository.save(home);
        return homeMapper.toDto(home);
    }

    /**
     * Get all the homes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<HomeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Homes");
        return homeRepository.findAll(pageable)
            .map(homeMapper::toDto);
    }

    /**
     * Get one home by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HomeDTO> findOne(Long id) {
        log.debug("Request to get Home : {}", id);
        return homeRepository.findById(id)
            .map(homeMapper::toDto);
    }

    /**
     * Delete the home by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Home : {}", id);
        homeRepository.deleteById(id);
    }
}
