package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.MitiendaApp;
import com.menttech.mitienda.domain.ShoppingCar;
import com.menttech.mitienda.domain.Customer;
import com.menttech.mitienda.repository.ShoppingCarRepository;
import com.menttech.mitienda.service.ShoppingCarService;
import com.menttech.mitienda.service.dto.ShoppingCarDTO;
import com.menttech.mitienda.service.mapper.ShoppingCarMapper;
import com.menttech.mitienda.service.dto.ShoppingCarCriteria;
import com.menttech.mitienda.service.ShoppingCarQueryService;

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
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ShoppingCarResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ShoppingCarResourceIT {

    private static final String DEFAULT_NUMBER_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);
    private static final BigDecimal SMALLER_QUANTITY = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_TOTAL_PURCHASE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PURCHASE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PURCHASE = new BigDecimal(1 - 1);

    @Autowired
    private ShoppingCarRepository shoppingCarRepository;

    @Autowired
    private ShoppingCarMapper shoppingCarMapper;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private ShoppingCarQueryService shoppingCarQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restShoppingCarMockMvc;

    private ShoppingCar shoppingCar;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCar createEntity(EntityManager em) {
        ShoppingCar shoppingCar = new ShoppingCar()
            .numberProduct(DEFAULT_NUMBER_PRODUCT)
            .product(DEFAULT_PRODUCT)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .totalPurchase(DEFAULT_TOTAL_PURCHASE);
        return shoppingCar;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ShoppingCar createUpdatedEntity(EntityManager em) {
        ShoppingCar shoppingCar = new ShoppingCar()
            .numberProduct(UPDATED_NUMBER_PRODUCT)
            .product(UPDATED_PRODUCT)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .totalPurchase(UPDATED_TOTAL_PURCHASE);
        return shoppingCar;
    }

    @BeforeEach
    public void initTest() {
        shoppingCar = createEntity(em);
    }

    @Test
    @Transactional
    public void createShoppingCar() throws Exception {
        int databaseSizeBeforeCreate = shoppingCarRepository.findAll().size();

        // Create the ShoppingCar
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);
        restShoppingCarMockMvc.perform(post("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isCreated());

        // Validate the ShoppingCar in the database
        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeCreate + 1);
        ShoppingCar testShoppingCar = shoppingCarList.get(shoppingCarList.size() - 1);
        assertThat(testShoppingCar.getNumberProduct()).isEqualTo(DEFAULT_NUMBER_PRODUCT);
        assertThat(testShoppingCar.getProduct()).isEqualTo(DEFAULT_PRODUCT);
        assertThat(testShoppingCar.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testShoppingCar.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testShoppingCar.getTotalPurchase()).isEqualTo(DEFAULT_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void createShoppingCarWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = shoppingCarRepository.findAll().size();

        // Create the ShoppingCar with an existing ID
        shoppingCar.setId(1L);
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);

        // An entity with an existing ID cannot be created, so this API call must fail
        restShoppingCarMockMvc.perform(post("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCar in the database
        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumberProductIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCarRepository.findAll().size();
        // set the field null
        shoppingCar.setNumberProduct(null);

        // Create the ShoppingCar, which fails.
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);

        restShoppingCarMockMvc.perform(post("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isBadRequest());

        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkProductIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCarRepository.findAll().size();
        // set the field null
        shoppingCar.setProduct(null);

        // Create the ShoppingCar, which fails.
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);

        restShoppingCarMockMvc.perform(post("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isBadRequest());

        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = shoppingCarRepository.findAll().size();
        // set the field null
        shoppingCar.setQuantity(null);

        // Create the ShoppingCar, which fails.
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);

        restShoppingCarMockMvc.perform(post("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isBadRequest());

        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllShoppingCars() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList
        restShoppingCarMockMvc.perform(get("/api/shopping-cars?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberProduct").value(hasItem(DEFAULT_NUMBER_PRODUCT)))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].totalPurchase").value(hasItem(DEFAULT_TOTAL_PURCHASE.intValue())));
    }
    
    @Test
    @Transactional
    public void getShoppingCar() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get the shoppingCar
        restShoppingCarMockMvc.perform(get("/api/shopping-cars/{id}", shoppingCar.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(shoppingCar.getId().intValue()))
            .andExpect(jsonPath("$.numberProduct").value(DEFAULT_NUMBER_PRODUCT))
            .andExpect(jsonPath("$.product").value(DEFAULT_PRODUCT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.totalPurchase").value(DEFAULT_TOTAL_PURCHASE.intValue()));
    }


    @Test
    @Transactional
    public void getShoppingCarsByIdFiltering() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        Long id = shoppingCar.getId();

        defaultShoppingCarShouldBeFound("id.equals=" + id);
        defaultShoppingCarShouldNotBeFound("id.notEquals=" + id);

        defaultShoppingCarShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultShoppingCarShouldNotBeFound("id.greaterThan=" + id);

        defaultShoppingCarShouldBeFound("id.lessThanOrEqual=" + id);
        defaultShoppingCarShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct equals to DEFAULT_NUMBER_PRODUCT
        defaultShoppingCarShouldBeFound("numberProduct.equals=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the shoppingCarList where numberProduct equals to UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldNotBeFound("numberProduct.equals=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct not equals to DEFAULT_NUMBER_PRODUCT
        defaultShoppingCarShouldNotBeFound("numberProduct.notEquals=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the shoppingCarList where numberProduct not equals to UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldBeFound("numberProduct.notEquals=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductIsInShouldWork() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct in DEFAULT_NUMBER_PRODUCT or UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldBeFound("numberProduct.in=" + DEFAULT_NUMBER_PRODUCT + "," + UPDATED_NUMBER_PRODUCT);

        // Get all the shoppingCarList where numberProduct equals to UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldNotBeFound("numberProduct.in=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct is not null
        defaultShoppingCarShouldBeFound("numberProduct.specified=true");

        // Get all the shoppingCarList where numberProduct is null
        defaultShoppingCarShouldNotBeFound("numberProduct.specified=false");
    }
                @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct contains DEFAULT_NUMBER_PRODUCT
        defaultShoppingCarShouldBeFound("numberProduct.contains=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the shoppingCarList where numberProduct contains UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldNotBeFound("numberProduct.contains=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByNumberProductNotContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where numberProduct does not contain DEFAULT_NUMBER_PRODUCT
        defaultShoppingCarShouldNotBeFound("numberProduct.doesNotContain=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the shoppingCarList where numberProduct does not contain UPDATED_NUMBER_PRODUCT
        defaultShoppingCarShouldBeFound("numberProduct.doesNotContain=" + UPDATED_NUMBER_PRODUCT);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByProductIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product equals to DEFAULT_PRODUCT
        defaultShoppingCarShouldBeFound("product.equals=" + DEFAULT_PRODUCT);

        // Get all the shoppingCarList where product equals to UPDATED_PRODUCT
        defaultShoppingCarShouldNotBeFound("product.equals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product not equals to DEFAULT_PRODUCT
        defaultShoppingCarShouldNotBeFound("product.notEquals=" + DEFAULT_PRODUCT);

        // Get all the shoppingCarList where product not equals to UPDATED_PRODUCT
        defaultShoppingCarShouldBeFound("product.notEquals=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByProductIsInShouldWork() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product in DEFAULT_PRODUCT or UPDATED_PRODUCT
        defaultShoppingCarShouldBeFound("product.in=" + DEFAULT_PRODUCT + "," + UPDATED_PRODUCT);

        // Get all the shoppingCarList where product equals to UPDATED_PRODUCT
        defaultShoppingCarShouldNotBeFound("product.in=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product is not null
        defaultShoppingCarShouldBeFound("product.specified=true");

        // Get all the shoppingCarList where product is null
        defaultShoppingCarShouldNotBeFound("product.specified=false");
    }
                @Test
    @Transactional
    public void getAllShoppingCarsByProductContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product contains DEFAULT_PRODUCT
        defaultShoppingCarShouldBeFound("product.contains=" + DEFAULT_PRODUCT);

        // Get all the shoppingCarList where product contains UPDATED_PRODUCT
        defaultShoppingCarShouldNotBeFound("product.contains=" + UPDATED_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByProductNotContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where product does not contain DEFAULT_PRODUCT
        defaultShoppingCarShouldNotBeFound("product.doesNotContain=" + DEFAULT_PRODUCT);

        // Get all the shoppingCarList where product does not contain UPDATED_PRODUCT
        defaultShoppingCarShouldBeFound("product.doesNotContain=" + UPDATED_PRODUCT);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description equals to DEFAULT_DESCRIPTION
        defaultShoppingCarShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the shoppingCarList where description equals to UPDATED_DESCRIPTION
        defaultShoppingCarShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description not equals to DEFAULT_DESCRIPTION
        defaultShoppingCarShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the shoppingCarList where description not equals to UPDATED_DESCRIPTION
        defaultShoppingCarShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultShoppingCarShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the shoppingCarList where description equals to UPDATED_DESCRIPTION
        defaultShoppingCarShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description is not null
        defaultShoppingCarShouldBeFound("description.specified=true");

        // Get all the shoppingCarList where description is null
        defaultShoppingCarShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description contains DEFAULT_DESCRIPTION
        defaultShoppingCarShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the shoppingCarList where description contains UPDATED_DESCRIPTION
        defaultShoppingCarShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where description does not contain DEFAULT_DESCRIPTION
        defaultShoppingCarShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the shoppingCarList where description does not contain UPDATED_DESCRIPTION
        defaultShoppingCarShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity equals to DEFAULT_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.equals=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity equals to UPDATED_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity not equals to DEFAULT_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.notEquals=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity not equals to UPDATED_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.notEquals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity in DEFAULT_QUANTITY or UPDATED_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY);

        // Get all the shoppingCarList where quantity equals to UPDATED_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity is not null
        defaultShoppingCarShouldBeFound("quantity.specified=true");

        // Get all the shoppingCarList where quantity is null
        defaultShoppingCarShouldNotBeFound("quantity.specified=false");
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity is greater than or equal to DEFAULT_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity is greater than or equal to UPDATED_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity is less than or equal to DEFAULT_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity is less than or equal to SMALLER_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity is less than DEFAULT_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.lessThan=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity is less than UPDATED_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.lessThan=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where quantity is greater than DEFAULT_QUANTITY
        defaultShoppingCarShouldNotBeFound("quantity.greaterThan=" + DEFAULT_QUANTITY);

        // Get all the shoppingCarList where quantity is greater than SMALLER_QUANTITY
        defaultShoppingCarShouldBeFound("quantity.greaterThan=" + SMALLER_QUANTITY);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase equals to DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.equals=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase equals to UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.equals=" + UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsNotEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase not equals to DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.notEquals=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase not equals to UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.notEquals=" + UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsInShouldWork() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase in DEFAULT_TOTAL_PURCHASE or UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.in=" + DEFAULT_TOTAL_PURCHASE + "," + UPDATED_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase equals to UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.in=" + UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsNullOrNotNull() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase is not null
        defaultShoppingCarShouldBeFound("totalPurchase.specified=true");

        // Get all the shoppingCarList where totalPurchase is null
        defaultShoppingCarShouldNotBeFound("totalPurchase.specified=false");
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase is greater than or equal to DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.greaterThanOrEqual=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase is greater than or equal to UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.greaterThanOrEqual=" + UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase is less than or equal to DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.lessThanOrEqual=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase is less than or equal to SMALLER_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.lessThanOrEqual=" + SMALLER_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsLessThanSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase is less than DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.lessThan=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase is less than UPDATED_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.lessThan=" + UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void getAllShoppingCarsByTotalPurchaseIsGreaterThanSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        // Get all the shoppingCarList where totalPurchase is greater than DEFAULT_TOTAL_PURCHASE
        defaultShoppingCarShouldNotBeFound("totalPurchase.greaterThan=" + DEFAULT_TOTAL_PURCHASE);

        // Get all the shoppingCarList where totalPurchase is greater than SMALLER_TOTAL_PURCHASE
        defaultShoppingCarShouldBeFound("totalPurchase.greaterThan=" + SMALLER_TOTAL_PURCHASE);
    }


    @Test
    @Transactional
    public void getAllShoppingCarsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);
        Customer user = CustomerResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        shoppingCar.setUser(user);
        shoppingCarRepository.saveAndFlush(shoppingCar);
        Long userId = user.getId();

        // Get all the shoppingCarList where user equals to userId
        defaultShoppingCarShouldBeFound("userId.equals=" + userId);

        // Get all the shoppingCarList where user equals to userId + 1
        defaultShoppingCarShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultShoppingCarShouldBeFound(String filter) throws Exception {
        restShoppingCarMockMvc.perform(get("/api/shopping-cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(shoppingCar.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberProduct").value(hasItem(DEFAULT_NUMBER_PRODUCT)))
            .andExpect(jsonPath("$.[*].product").value(hasItem(DEFAULT_PRODUCT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].totalPurchase").value(hasItem(DEFAULT_TOTAL_PURCHASE.intValue())));

        // Check, that the count call also returns 1
        restShoppingCarMockMvc.perform(get("/api/shopping-cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultShoppingCarShouldNotBeFound(String filter) throws Exception {
        restShoppingCarMockMvc.perform(get("/api/shopping-cars?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restShoppingCarMockMvc.perform(get("/api/shopping-cars/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingShoppingCar() throws Exception {
        // Get the shoppingCar
        restShoppingCarMockMvc.perform(get("/api/shopping-cars/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateShoppingCar() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        int databaseSizeBeforeUpdate = shoppingCarRepository.findAll().size();

        // Update the shoppingCar
        ShoppingCar updatedShoppingCar = shoppingCarRepository.findById(shoppingCar.getId()).get();
        // Disconnect from session so that the updates on updatedShoppingCar are not directly saved in db
        em.detach(updatedShoppingCar);
        updatedShoppingCar
            .numberProduct(UPDATED_NUMBER_PRODUCT)
            .product(UPDATED_PRODUCT)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .totalPurchase(UPDATED_TOTAL_PURCHASE);
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(updatedShoppingCar);

        restShoppingCarMockMvc.perform(put("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isOk());

        // Validate the ShoppingCar in the database
        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeUpdate);
        ShoppingCar testShoppingCar = shoppingCarList.get(shoppingCarList.size() - 1);
        assertThat(testShoppingCar.getNumberProduct()).isEqualTo(UPDATED_NUMBER_PRODUCT);
        assertThat(testShoppingCar.getProduct()).isEqualTo(UPDATED_PRODUCT);
        assertThat(testShoppingCar.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testShoppingCar.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testShoppingCar.getTotalPurchase()).isEqualTo(UPDATED_TOTAL_PURCHASE);
    }

    @Test
    @Transactional
    public void updateNonExistingShoppingCar() throws Exception {
        int databaseSizeBeforeUpdate = shoppingCarRepository.findAll().size();

        // Create the ShoppingCar
        ShoppingCarDTO shoppingCarDTO = shoppingCarMapper.toDto(shoppingCar);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restShoppingCarMockMvc.perform(put("/api/shopping-cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(shoppingCarDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ShoppingCar in the database
        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteShoppingCar() throws Exception {
        // Initialize the database
        shoppingCarRepository.saveAndFlush(shoppingCar);

        int databaseSizeBeforeDelete = shoppingCarRepository.findAll().size();

        // Delete the shoppingCar
        restShoppingCarMockMvc.perform(delete("/api/shopping-cars/{id}", shoppingCar.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ShoppingCar> shoppingCarList = shoppingCarRepository.findAll();
        assertThat(shoppingCarList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
