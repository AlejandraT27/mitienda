package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.MitiendaApp;
import com.menttech.mitienda.domain.Home;
import com.menttech.mitienda.repository.HomeRepository;
import com.menttech.mitienda.service.HomeService;
import com.menttech.mitienda.service.dto.HomeDTO;
import com.menttech.mitienda.service.mapper.HomeMapper;
import com.menttech.mitienda.service.dto.HomeCriteria;
import com.menttech.mitienda.service.HomeQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link HomeResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class HomeResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_LOCALITY = "AAAAAAAAAA";
    private static final String UPDATED_LOCALITY = "BBBBBBBBBB";

    @Autowired
    private HomeRepository homeRepository;

    @Autowired
    private HomeMapper homeMapper;

    @Autowired
    private HomeService homeService;

    @Autowired
    private HomeQueryService homeQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHomeMockMvc;

    private Home home;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Home createEntity(EntityManager em) {
        Home home = new Home()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .adress(DEFAULT_ADRESS)
            .phone(DEFAULT_PHONE)
            .locality(DEFAULT_LOCALITY);
        return home;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Home createUpdatedEntity(EntityManager em) {
        Home home = new Home()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .adress(UPDATED_ADRESS)
            .phone(UPDATED_PHONE)
            .locality(UPDATED_LOCALITY);
        return home;
    }

    @BeforeEach
    public void initTest() {
        home = createEntity(em);
    }

    @Test
    @Transactional
    public void createHome() throws Exception {
        int databaseSizeBeforeCreate = homeRepository.findAll().size();

        // Create the Home
        HomeDTO homeDTO = homeMapper.toDto(home);
        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isCreated());

        // Validate the Home in the database
        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeCreate + 1);
        Home testHome = homeList.get(homeList.size() - 1);
        assertThat(testHome.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testHome.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testHome.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testHome.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testHome.getLocality()).isEqualTo(DEFAULT_LOCALITY);
    }

    @Test
    @Transactional
    public void createHomeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = homeRepository.findAll().size();

        // Create the Home with an existing ID
        home.setId(1L);
        HomeDTO homeDTO = homeMapper.toDto(home);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Home in the database
        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeRepository.findAll().size();
        // set the field null
        home.setFirstName(null);

        // Create the Home, which fails.
        HomeDTO homeDTO = homeMapper.toDto(home);

        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeRepository.findAll().size();
        // set the field null
        home.setLastName(null);

        // Create the Home, which fails.
        HomeDTO homeDTO = homeMapper.toDto(home);

        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeRepository.findAll().size();
        // set the field null
        home.setAdress(null);

        // Create the Home, which fails.
        HomeDTO homeDTO = homeMapper.toDto(home);

        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = homeRepository.findAll().size();
        // set the field null
        home.setPhone(null);

        // Create the Home, which fails.
        HomeDTO homeDTO = homeMapper.toDto(home);

        restHomeMockMvc.perform(post("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHomes() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList
        restHomeMockMvc.perform(get("/api/homes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(home.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)));
    }
    
    @Test
    @Transactional
    public void getHome() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get the home
        restHomeMockMvc.perform(get("/api/homes/{id}", home.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(home.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.locality").value(DEFAULT_LOCALITY));
    }


    @Test
    @Transactional
    public void getHomesByIdFiltering() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        Long id = home.getId();

        defaultHomeShouldBeFound("id.equals=" + id);
        defaultHomeShouldNotBeFound("id.notEquals=" + id);

        defaultHomeShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultHomeShouldNotBeFound("id.greaterThan=" + id);

        defaultHomeShouldBeFound("id.lessThanOrEqual=" + id);
        defaultHomeShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllHomesByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName equals to DEFAULT_FIRST_NAME
        defaultHomeShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the homeList where firstName equals to UPDATED_FIRST_NAME
        defaultHomeShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByFirstNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName not equals to DEFAULT_FIRST_NAME
        defaultHomeShouldNotBeFound("firstName.notEquals=" + DEFAULT_FIRST_NAME);

        // Get all the homeList where firstName not equals to UPDATED_FIRST_NAME
        defaultHomeShouldBeFound("firstName.notEquals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultHomeShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the homeList where firstName equals to UPDATED_FIRST_NAME
        defaultHomeShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName is not null
        defaultHomeShouldBeFound("firstName.specified=true");

        // Get all the homeList where firstName is null
        defaultHomeShouldNotBeFound("firstName.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomesByFirstNameContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName contains DEFAULT_FIRST_NAME
        defaultHomeShouldBeFound("firstName.contains=" + DEFAULT_FIRST_NAME);

        // Get all the homeList where firstName contains UPDATED_FIRST_NAME
        defaultHomeShouldNotBeFound("firstName.contains=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByFirstNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where firstName does not contain DEFAULT_FIRST_NAME
        defaultHomeShouldNotBeFound("firstName.doesNotContain=" + DEFAULT_FIRST_NAME);

        // Get all the homeList where firstName does not contain UPDATED_FIRST_NAME
        defaultHomeShouldBeFound("firstName.doesNotContain=" + UPDATED_FIRST_NAME);
    }


    @Test
    @Transactional
    public void getAllHomesByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName equals to DEFAULT_LAST_NAME
        defaultHomeShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the homeList where lastName equals to UPDATED_LAST_NAME
        defaultHomeShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByLastNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName not equals to DEFAULT_LAST_NAME
        defaultHomeShouldNotBeFound("lastName.notEquals=" + DEFAULT_LAST_NAME);

        // Get all the homeList where lastName not equals to UPDATED_LAST_NAME
        defaultHomeShouldBeFound("lastName.notEquals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultHomeShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the homeList where lastName equals to UPDATED_LAST_NAME
        defaultHomeShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName is not null
        defaultHomeShouldBeFound("lastName.specified=true");

        // Get all the homeList where lastName is null
        defaultHomeShouldNotBeFound("lastName.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomesByLastNameContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName contains DEFAULT_LAST_NAME
        defaultHomeShouldBeFound("lastName.contains=" + DEFAULT_LAST_NAME);

        // Get all the homeList where lastName contains UPDATED_LAST_NAME
        defaultHomeShouldNotBeFound("lastName.contains=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllHomesByLastNameNotContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where lastName does not contain DEFAULT_LAST_NAME
        defaultHomeShouldNotBeFound("lastName.doesNotContain=" + DEFAULT_LAST_NAME);

        // Get all the homeList where lastName does not contain UPDATED_LAST_NAME
        defaultHomeShouldBeFound("lastName.doesNotContain=" + UPDATED_LAST_NAME);
    }


    @Test
    @Transactional
    public void getAllHomesByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress equals to DEFAULT_ADRESS
        defaultHomeShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the homeList where adress equals to UPDATED_ADRESS
        defaultHomeShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllHomesByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress not equals to DEFAULT_ADRESS
        defaultHomeShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the homeList where adress not equals to UPDATED_ADRESS
        defaultHomeShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllHomesByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultHomeShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the homeList where adress equals to UPDATED_ADRESS
        defaultHomeShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllHomesByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress is not null
        defaultHomeShouldBeFound("adress.specified=true");

        // Get all the homeList where adress is null
        defaultHomeShouldNotBeFound("adress.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomesByAdressContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress contains DEFAULT_ADRESS
        defaultHomeShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the homeList where adress contains UPDATED_ADRESS
        defaultHomeShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllHomesByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where adress does not contain DEFAULT_ADRESS
        defaultHomeShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the homeList where adress does not contain UPDATED_ADRESS
        defaultHomeShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }


    @Test
    @Transactional
    public void getAllHomesByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone equals to DEFAULT_PHONE
        defaultHomeShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the homeList where phone equals to UPDATED_PHONE
        defaultHomeShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomesByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone not equals to DEFAULT_PHONE
        defaultHomeShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the homeList where phone not equals to UPDATED_PHONE
        defaultHomeShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomesByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultHomeShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the homeList where phone equals to UPDATED_PHONE
        defaultHomeShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomesByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone is not null
        defaultHomeShouldBeFound("phone.specified=true");

        // Get all the homeList where phone is null
        defaultHomeShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomesByPhoneContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone contains DEFAULT_PHONE
        defaultHomeShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the homeList where phone contains UPDATED_PHONE
        defaultHomeShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllHomesByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where phone does not contain DEFAULT_PHONE
        defaultHomeShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the homeList where phone does not contain UPDATED_PHONE
        defaultHomeShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllHomesByLocalityIsEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality equals to DEFAULT_LOCALITY
        defaultHomeShouldBeFound("locality.equals=" + DEFAULT_LOCALITY);

        // Get all the homeList where locality equals to UPDATED_LOCALITY
        defaultHomeShouldNotBeFound("locality.equals=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    public void getAllHomesByLocalityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality not equals to DEFAULT_LOCALITY
        defaultHomeShouldNotBeFound("locality.notEquals=" + DEFAULT_LOCALITY);

        // Get all the homeList where locality not equals to UPDATED_LOCALITY
        defaultHomeShouldBeFound("locality.notEquals=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    public void getAllHomesByLocalityIsInShouldWork() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality in DEFAULT_LOCALITY or UPDATED_LOCALITY
        defaultHomeShouldBeFound("locality.in=" + DEFAULT_LOCALITY + "," + UPDATED_LOCALITY);

        // Get all the homeList where locality equals to UPDATED_LOCALITY
        defaultHomeShouldNotBeFound("locality.in=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    public void getAllHomesByLocalityIsNullOrNotNull() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality is not null
        defaultHomeShouldBeFound("locality.specified=true");

        // Get all the homeList where locality is null
        defaultHomeShouldNotBeFound("locality.specified=false");
    }
                @Test
    @Transactional
    public void getAllHomesByLocalityContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality contains DEFAULT_LOCALITY
        defaultHomeShouldBeFound("locality.contains=" + DEFAULT_LOCALITY);

        // Get all the homeList where locality contains UPDATED_LOCALITY
        defaultHomeShouldNotBeFound("locality.contains=" + UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    public void getAllHomesByLocalityNotContainsSomething() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        // Get all the homeList where locality does not contain DEFAULT_LOCALITY
        defaultHomeShouldNotBeFound("locality.doesNotContain=" + DEFAULT_LOCALITY);

        // Get all the homeList where locality does not contain UPDATED_LOCALITY
        defaultHomeShouldBeFound("locality.doesNotContain=" + UPDATED_LOCALITY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHomeShouldBeFound(String filter) throws Exception {
        restHomeMockMvc.perform(get("/api/homes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(home.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].locality").value(hasItem(DEFAULT_LOCALITY)));

        // Check, that the count call also returns 1
        restHomeMockMvc.perform(get("/api/homes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHomeShouldNotBeFound(String filter) throws Exception {
        restHomeMockMvc.perform(get("/api/homes?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHomeMockMvc.perform(get("/api/homes/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingHome() throws Exception {
        // Get the home
        restHomeMockMvc.perform(get("/api/homes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHome() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        int databaseSizeBeforeUpdate = homeRepository.findAll().size();

        // Update the home
        Home updatedHome = homeRepository.findById(home.getId()).get();
        // Disconnect from session so that the updates on updatedHome are not directly saved in db
        em.detach(updatedHome);
        updatedHome
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .adress(UPDATED_ADRESS)
            .phone(UPDATED_PHONE)
            .locality(UPDATED_LOCALITY);
        HomeDTO homeDTO = homeMapper.toDto(updatedHome);

        restHomeMockMvc.perform(put("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isOk());

        // Validate the Home in the database
        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeUpdate);
        Home testHome = homeList.get(homeList.size() - 1);
        assertThat(testHome.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testHome.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testHome.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testHome.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testHome.getLocality()).isEqualTo(UPDATED_LOCALITY);
    }

    @Test
    @Transactional
    public void updateNonExistingHome() throws Exception {
        int databaseSizeBeforeUpdate = homeRepository.findAll().size();

        // Create the Home
        HomeDTO homeDTO = homeMapper.toDto(home);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomeMockMvc.perform(put("/api/homes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(homeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Home in the database
        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHome() throws Exception {
        // Initialize the database
        homeRepository.saveAndFlush(home);

        int databaseSizeBeforeDelete = homeRepository.findAll().size();

        // Delete the home
        restHomeMockMvc.perform(delete("/api/homes/{id}", home.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Home> homeList = homeRepository.findAll();
        assertThat(homeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
