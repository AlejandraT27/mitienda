package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.service.ShoppingCarService;
import com.menttech.mitienda.web.rest.errors.BadRequestAlertException;
import com.menttech.mitienda.service.dto.ShoppingCarDTO;
import com.menttech.mitienda.service.dto.ShoppingCarCriteria;
import com.menttech.mitienda.service.ShoppingCarQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.menttech.mitienda.domain.ShoppingCar}.
 */
@RestController
@RequestMapping("/api")
public class ShoppingCarResource {

    private final Logger log = LoggerFactory.getLogger(ShoppingCarResource.class);

    private static final String ENTITY_NAME = "shoppingCar";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShoppingCarService shoppingCarService;

    private final ShoppingCarQueryService shoppingCarQueryService;

    public ShoppingCarResource(ShoppingCarService shoppingCarService, ShoppingCarQueryService shoppingCarQueryService) {
        this.shoppingCarService = shoppingCarService;
        this.shoppingCarQueryService = shoppingCarQueryService;
    }

    /**
     * {@code POST  /shopping-cars} : Create a new shoppingCar.
     *
     * @param shoppingCarDTO the shoppingCarDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new shoppingCarDTO, or with status {@code 400 (Bad Request)} if the shoppingCar has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/shopping-cars")
    public ResponseEntity<ShoppingCarDTO> createShoppingCar(@Valid @RequestBody ShoppingCarDTO shoppingCarDTO) throws URISyntaxException {
        log.debug("REST request to save ShoppingCar : {}", shoppingCarDTO);
        if (shoppingCarDTO.getId() != null) {
            throw new BadRequestAlertException("A new shoppingCar cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ShoppingCarDTO result = shoppingCarService.save(shoppingCarDTO);
        return ResponseEntity.created(new URI("/api/shopping-cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /shopping-cars} : Updates an existing shoppingCar.
     *
     * @param shoppingCarDTO the shoppingCarDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated shoppingCarDTO,
     * or with status {@code 400 (Bad Request)} if the shoppingCarDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shoppingCarDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/shopping-cars")
    public ResponseEntity<ShoppingCarDTO> updateShoppingCar(@Valid @RequestBody ShoppingCarDTO shoppingCarDTO) throws URISyntaxException {
        log.debug("REST request to update ShoppingCar : {}", shoppingCarDTO);
        if (shoppingCarDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ShoppingCarDTO result = shoppingCarService.save(shoppingCarDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, shoppingCarDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /shopping-cars} : get all the shoppingCars.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of shoppingCars in body.
     */
    @GetMapping("/shopping-cars")
    public ResponseEntity<List<ShoppingCarDTO>> getAllShoppingCars(ShoppingCarCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ShoppingCars by criteria: {}", criteria);
        Page<ShoppingCarDTO> page = shoppingCarQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /shopping-cars/count} : count all the shoppingCars.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/shopping-cars/count")
    public ResponseEntity<Long> countShoppingCars(ShoppingCarCriteria criteria) {
        log.debug("REST request to count ShoppingCars by criteria: {}", criteria);
        return ResponseEntity.ok().body(shoppingCarQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /shopping-cars/:id} : get the "id" shoppingCar.
     *
     * @param id the id of the shoppingCarDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the shoppingCarDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/shopping-cars/{id}")
    public ResponseEntity<ShoppingCarDTO> getShoppingCar(@PathVariable Long id) {
        log.debug("REST request to get ShoppingCar : {}", id);
        Optional<ShoppingCarDTO> shoppingCarDTO = shoppingCarService.findOne(id);
        return ResponseUtil.wrapOrNotFound(shoppingCarDTO);
    }

    /**
     * {@code DELETE  /shopping-cars/:id} : delete the "id" shoppingCar.
     *
     * @param id the id of the shoppingCarDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/shopping-cars/{id}")
    public ResponseEntity<Void> deleteShoppingCar(@PathVariable Long id) {
        log.debug("REST request to delete ShoppingCar : {}", id);
        shoppingCarService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
