package com.menttech.mitienda.service;

import com.menttech.mitienda.domain.ShoppingCar;
import com.menttech.mitienda.repository.ShoppingCarRepository;
import com.menttech.mitienda.service.dto.ShoppingCarDTO;
import com.menttech.mitienda.service.mapper.ShoppingCarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ShoppingCar}.
 */
@Service
@Transactional
public class ShoppingCarService {

    private final Logger log = LoggerFactory.getLogger(ShoppingCarService.class);

    private final ShoppingCarRepository shoppingCarRepository;

    private final ShoppingCarMapper shoppingCarMapper;

    public ShoppingCarService(ShoppingCarRepository shoppingCarRepository, ShoppingCarMapper shoppingCarMapper) {
        this.shoppingCarRepository = shoppingCarRepository;
        this.shoppingCarMapper = shoppingCarMapper;
    }

    /**
     * Save a shoppingCar.
     *
     * @param shoppingCarDTO the entity to save.
     * @return the persisted entity.
     */
    public ShoppingCarDTO save(ShoppingCarDTO shoppingCarDTO) {
        log.debug("Request to save ShoppingCar : {}", shoppingCarDTO);
        ShoppingCar shoppingCar = shoppingCarMapper.toEntity(shoppingCarDTO);
        shoppingCar = shoppingCarRepository.save(shoppingCar);
        return shoppingCarMapper.toDto(shoppingCar);
    }

    /**
     * Get all the shoppingCars.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ShoppingCarDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ShoppingCars");
        return shoppingCarRepository.findAll(pageable)
            .map(shoppingCarMapper::toDto);
    }

    /**
     * Get one shoppingCar by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ShoppingCarDTO> findOne(Long id) {
        log.debug("Request to get ShoppingCar : {}", id);
        return shoppingCarRepository.findById(id)
            .map(shoppingCarMapper::toDto);
    }

    /**
     * Delete the shoppingCar by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ShoppingCar : {}", id);
        shoppingCarRepository.deleteById(id);
    }
}
