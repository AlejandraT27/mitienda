package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.service.HomeService;
import com.menttech.mitienda.web.rest.errors.BadRequestAlertException;
import com.menttech.mitienda.service.dto.HomeDTO;
import com.menttech.mitienda.service.dto.HomeCriteria;
import com.menttech.mitienda.service.HomeQueryService;

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
 * REST controller for managing {@link com.menttech.mitienda.domain.Home}.
 */
@RestController
@RequestMapping("/api")
public class HomeResource {

    private final Logger log = LoggerFactory.getLogger(HomeResource.class);

    private static final String ENTITY_NAME = "home";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HomeService homeService;

    private final HomeQueryService homeQueryService;

    public HomeResource(HomeService homeService, HomeQueryService homeQueryService) {
        this.homeService = homeService;
        this.homeQueryService = homeQueryService;
    }

    /**
     * {@code POST  /homes} : Create a new home.
     *
     * @param homeDTO the homeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new homeDTO, or with status {@code 400 (Bad Request)} if the home has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/homes")
    public ResponseEntity<HomeDTO> createHome(@Valid @RequestBody HomeDTO homeDTO) throws URISyntaxException {
        log.debug("REST request to save Home : {}", homeDTO);
        if (homeDTO.getId() != null) {
            throw new BadRequestAlertException("A new home cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HomeDTO result = homeService.save(homeDTO);
        return ResponseEntity.created(new URI("/api/homes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /homes} : Updates an existing home.
     *
     * @param homeDTO the homeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated homeDTO,
     * or with status {@code 400 (Bad Request)} if the homeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the homeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/homes")
    public ResponseEntity<HomeDTO> updateHome(@Valid @RequestBody HomeDTO homeDTO) throws URISyntaxException {
        log.debug("REST request to update Home : {}", homeDTO);
        if (homeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HomeDTO result = homeService.save(homeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, homeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /homes} : get all the homes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of homes in body.
     */
    @GetMapping("/homes")
    public ResponseEntity<List<HomeDTO>> getAllHomes(HomeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Homes by criteria: {}", criteria);
        Page<HomeDTO> page = homeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /homes/count} : count all the homes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/homes/count")
    public ResponseEntity<Long> countHomes(HomeCriteria criteria) {
        log.debug("REST request to count Homes by criteria: {}", criteria);
        return ResponseEntity.ok().body(homeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /homes/:id} : get the "id" home.
     *
     * @param id the id of the homeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the homeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/homes/{id}")
    public ResponseEntity<HomeDTO> getHome(@PathVariable Long id) {
        log.debug("REST request to get Home : {}", id);
        Optional<HomeDTO> homeDTO = homeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(homeDTO);
    }

    /**
     * {@code DELETE  /homes/:id} : delete the "id" home.
     *
     * @param id the id of the homeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/homes/{id}")
    public ResponseEntity<Void> deleteHome(@PathVariable Long id) {
        log.debug("REST request to delete Home : {}", id);
        homeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
