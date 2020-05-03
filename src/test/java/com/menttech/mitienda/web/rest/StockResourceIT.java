package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.MitiendaApp;
import com.menttech.mitienda.domain.Stock;
import com.menttech.mitienda.repository.StockRepository;
import com.menttech.mitienda.service.StockService;
import com.menttech.mitienda.service.dto.StockDTO;
import com.menttech.mitienda.service.mapper.StockMapper;
import com.menttech.mitienda.service.dto.StockCriteria;
import com.menttech.mitienda.service.StockQueryService;

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
 * Integration tests for the {@link StockResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class StockResourceIT {

    private static final String DEFAULT_NUMBER_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_NUMBER_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_NAME_PRODUCT = "AAAAAAAAAA";
    private static final String UPDATED_NAME_PRODUCT = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_INVENTORY = "AAAAAAAAAA";
    private static final String UPDATED_INVENTORY = "BBBBBBBBBB";

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private StockService stockService;

    @Autowired
    private StockQueryService stockQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStockMockMvc;

    private Stock stock;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createEntity(EntityManager em) {
        Stock stock = new Stock()
            .numberProduct(DEFAULT_NUMBER_PRODUCT)
            .nameProduct(DEFAULT_NAME_PRODUCT)
            .description(DEFAULT_DESCRIPTION)
            .inventory(DEFAULT_INVENTORY);
        return stock;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Stock createUpdatedEntity(EntityManager em) {
        Stock stock = new Stock()
            .numberProduct(UPDATED_NUMBER_PRODUCT)
            .nameProduct(UPDATED_NAME_PRODUCT)
            .description(UPDATED_DESCRIPTION)
            .inventory(UPDATED_INVENTORY);
        return stock;
    }

    @BeforeEach
    public void initTest() {
        stock = createEntity(em);
    }

    @Test
    @Transactional
    public void createStock() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isCreated());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate + 1);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getNumberProduct()).isEqualTo(DEFAULT_NUMBER_PRODUCT);
        assertThat(testStock.getNameProduct()).isEqualTo(DEFAULT_NAME_PRODUCT);
        assertThat(testStock.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testStock.getInventory()).isEqualTo(DEFAULT_INVENTORY);
    }

    @Test
    @Transactional
    public void createStockWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = stockRepository.findAll().size();

        // Create the Stock with an existing ID
        stock.setId(1L);
        StockDTO stockDTO = stockMapper.toDto(stock);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNumberProductIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setNumberProduct(null);

        // Create the Stock, which fails.
        StockDTO stockDTO = stockMapper.toDto(stock);

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkNameProductIsRequired() throws Exception {
        int databaseSizeBeforeTest = stockRepository.findAll().size();
        // set the field null
        stock.setNameProduct(null);

        // Create the Stock, which fails.
        StockDTO stockDTO = stockMapper.toDto(stock);

        restStockMockMvc.perform(post("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStocks() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberProduct").value(hasItem(DEFAULT_NUMBER_PRODUCT)))
            .andExpect(jsonPath("$.[*].nameProduct").value(hasItem(DEFAULT_NAME_PRODUCT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inventory").value(hasItem(DEFAULT_INVENTORY)));
    }
    
    @Test
    @Transactional
    public void getStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", stock.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stock.getId().intValue()))
            .andExpect(jsonPath("$.numberProduct").value(DEFAULT_NUMBER_PRODUCT))
            .andExpect(jsonPath("$.nameProduct").value(DEFAULT_NAME_PRODUCT))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.inventory").value(DEFAULT_INVENTORY));
    }


    @Test
    @Transactional
    public void getStocksByIdFiltering() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        Long id = stock.getId();

        defaultStockShouldBeFound("id.equals=" + id);
        defaultStockShouldNotBeFound("id.notEquals=" + id);

        defaultStockShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.greaterThan=" + id);

        defaultStockShouldBeFound("id.lessThanOrEqual=" + id);
        defaultStockShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllStocksByNumberProductIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct equals to DEFAULT_NUMBER_PRODUCT
        defaultStockShouldBeFound("numberProduct.equals=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the stockList where numberProduct equals to UPDATED_NUMBER_PRODUCT
        defaultStockShouldNotBeFound("numberProduct.equals=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNumberProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct not equals to DEFAULT_NUMBER_PRODUCT
        defaultStockShouldNotBeFound("numberProduct.notEquals=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the stockList where numberProduct not equals to UPDATED_NUMBER_PRODUCT
        defaultStockShouldBeFound("numberProduct.notEquals=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNumberProductIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct in DEFAULT_NUMBER_PRODUCT or UPDATED_NUMBER_PRODUCT
        defaultStockShouldBeFound("numberProduct.in=" + DEFAULT_NUMBER_PRODUCT + "," + UPDATED_NUMBER_PRODUCT);

        // Get all the stockList where numberProduct equals to UPDATED_NUMBER_PRODUCT
        defaultStockShouldNotBeFound("numberProduct.in=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNumberProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct is not null
        defaultStockShouldBeFound("numberProduct.specified=true");

        // Get all the stockList where numberProduct is null
        defaultStockShouldNotBeFound("numberProduct.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByNumberProductContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct contains DEFAULT_NUMBER_PRODUCT
        defaultStockShouldBeFound("numberProduct.contains=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the stockList where numberProduct contains UPDATED_NUMBER_PRODUCT
        defaultStockShouldNotBeFound("numberProduct.contains=" + UPDATED_NUMBER_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNumberProductNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where numberProduct does not contain DEFAULT_NUMBER_PRODUCT
        defaultStockShouldNotBeFound("numberProduct.doesNotContain=" + DEFAULT_NUMBER_PRODUCT);

        // Get all the stockList where numberProduct does not contain UPDATED_NUMBER_PRODUCT
        defaultStockShouldBeFound("numberProduct.doesNotContain=" + UPDATED_NUMBER_PRODUCT);
    }


    @Test
    @Transactional
    public void getAllStocksByNameProductIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct equals to DEFAULT_NAME_PRODUCT
        defaultStockShouldBeFound("nameProduct.equals=" + DEFAULT_NAME_PRODUCT);

        // Get all the stockList where nameProduct equals to UPDATED_NAME_PRODUCT
        defaultStockShouldNotBeFound("nameProduct.equals=" + UPDATED_NAME_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNameProductIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct not equals to DEFAULT_NAME_PRODUCT
        defaultStockShouldNotBeFound("nameProduct.notEquals=" + DEFAULT_NAME_PRODUCT);

        // Get all the stockList where nameProduct not equals to UPDATED_NAME_PRODUCT
        defaultStockShouldBeFound("nameProduct.notEquals=" + UPDATED_NAME_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNameProductIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct in DEFAULT_NAME_PRODUCT or UPDATED_NAME_PRODUCT
        defaultStockShouldBeFound("nameProduct.in=" + DEFAULT_NAME_PRODUCT + "," + UPDATED_NAME_PRODUCT);

        // Get all the stockList where nameProduct equals to UPDATED_NAME_PRODUCT
        defaultStockShouldNotBeFound("nameProduct.in=" + UPDATED_NAME_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNameProductIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct is not null
        defaultStockShouldBeFound("nameProduct.specified=true");

        // Get all the stockList where nameProduct is null
        defaultStockShouldNotBeFound("nameProduct.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByNameProductContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct contains DEFAULT_NAME_PRODUCT
        defaultStockShouldBeFound("nameProduct.contains=" + DEFAULT_NAME_PRODUCT);

        // Get all the stockList where nameProduct contains UPDATED_NAME_PRODUCT
        defaultStockShouldNotBeFound("nameProduct.contains=" + UPDATED_NAME_PRODUCT);
    }

    @Test
    @Transactional
    public void getAllStocksByNameProductNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where nameProduct does not contain DEFAULT_NAME_PRODUCT
        defaultStockShouldNotBeFound("nameProduct.doesNotContain=" + DEFAULT_NAME_PRODUCT);

        // Get all the stockList where nameProduct does not contain UPDATED_NAME_PRODUCT
        defaultStockShouldBeFound("nameProduct.doesNotContain=" + UPDATED_NAME_PRODUCT);
    }


    @Test
    @Transactional
    public void getAllStocksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description equals to DEFAULT_DESCRIPTION
        defaultStockShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the stockList where description equals to UPDATED_DESCRIPTION
        defaultStockShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description not equals to DEFAULT_DESCRIPTION
        defaultStockShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the stockList where description not equals to UPDATED_DESCRIPTION
        defaultStockShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultStockShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the stockList where description equals to UPDATED_DESCRIPTION
        defaultStockShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description is not null
        defaultStockShouldBeFound("description.specified=true");

        // Get all the stockList where description is null
        defaultStockShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description contains DEFAULT_DESCRIPTION
        defaultStockShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the stockList where description contains UPDATED_DESCRIPTION
        defaultStockShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllStocksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where description does not contain DEFAULT_DESCRIPTION
        defaultStockShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the stockList where description does not contain UPDATED_DESCRIPTION
        defaultStockShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllStocksByInventoryIsEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory equals to DEFAULT_INVENTORY
        defaultStockShouldBeFound("inventory.equals=" + DEFAULT_INVENTORY);

        // Get all the stockList where inventory equals to UPDATED_INVENTORY
        defaultStockShouldNotBeFound("inventory.equals=" + UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void getAllStocksByInventoryIsNotEqualToSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory not equals to DEFAULT_INVENTORY
        defaultStockShouldNotBeFound("inventory.notEquals=" + DEFAULT_INVENTORY);

        // Get all the stockList where inventory not equals to UPDATED_INVENTORY
        defaultStockShouldBeFound("inventory.notEquals=" + UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void getAllStocksByInventoryIsInShouldWork() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory in DEFAULT_INVENTORY or UPDATED_INVENTORY
        defaultStockShouldBeFound("inventory.in=" + DEFAULT_INVENTORY + "," + UPDATED_INVENTORY);

        // Get all the stockList where inventory equals to UPDATED_INVENTORY
        defaultStockShouldNotBeFound("inventory.in=" + UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void getAllStocksByInventoryIsNullOrNotNull() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory is not null
        defaultStockShouldBeFound("inventory.specified=true");

        // Get all the stockList where inventory is null
        defaultStockShouldNotBeFound("inventory.specified=false");
    }
                @Test
    @Transactional
    public void getAllStocksByInventoryContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory contains DEFAULT_INVENTORY
        defaultStockShouldBeFound("inventory.contains=" + DEFAULT_INVENTORY);

        // Get all the stockList where inventory contains UPDATED_INVENTORY
        defaultStockShouldNotBeFound("inventory.contains=" + UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void getAllStocksByInventoryNotContainsSomething() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        // Get all the stockList where inventory does not contain DEFAULT_INVENTORY
        defaultStockShouldNotBeFound("inventory.doesNotContain=" + DEFAULT_INVENTORY);

        // Get all the stockList where inventory does not contain UPDATED_INVENTORY
        defaultStockShouldBeFound("inventory.doesNotContain=" + UPDATED_INVENTORY);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultStockShouldBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stock.getId().intValue())))
            .andExpect(jsonPath("$.[*].numberProduct").value(hasItem(DEFAULT_NUMBER_PRODUCT)))
            .andExpect(jsonPath("$.[*].nameProduct").value(hasItem(DEFAULT_NAME_PRODUCT)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].inventory").value(hasItem(DEFAULT_INVENTORY)));

        // Check, that the count call also returns 1
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultStockShouldNotBeFound(String filter) throws Exception {
        restStockMockMvc.perform(get("/api/stocks?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restStockMockMvc.perform(get("/api/stocks/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingStock() throws Exception {
        // Get the stock
        restStockMockMvc.perform(get("/api/stocks/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Update the stock
        Stock updatedStock = stockRepository.findById(stock.getId()).get();
        // Disconnect from session so that the updates on updatedStock are not directly saved in db
        em.detach(updatedStock);
        updatedStock
            .numberProduct(UPDATED_NUMBER_PRODUCT)
            .nameProduct(UPDATED_NAME_PRODUCT)
            .description(UPDATED_DESCRIPTION)
            .inventory(UPDATED_INVENTORY);
        StockDTO stockDTO = stockMapper.toDto(updatedStock);

        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isOk());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
        Stock testStock = stockList.get(stockList.size() - 1);
        assertThat(testStock.getNumberProduct()).isEqualTo(UPDATED_NUMBER_PRODUCT);
        assertThat(testStock.getNameProduct()).isEqualTo(UPDATED_NAME_PRODUCT);
        assertThat(testStock.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testStock.getInventory()).isEqualTo(UPDATED_INVENTORY);
    }

    @Test
    @Transactional
    public void updateNonExistingStock() throws Exception {
        int databaseSizeBeforeUpdate = stockRepository.findAll().size();

        // Create the Stock
        StockDTO stockDTO = stockMapper.toDto(stock);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStockMockMvc.perform(put("/api/stocks")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(stockDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Stock in the database
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteStock() throws Exception {
        // Initialize the database
        stockRepository.saveAndFlush(stock);

        int databaseSizeBeforeDelete = stockRepository.findAll().size();

        // Delete the stock
        restStockMockMvc.perform(delete("/api/stocks/{id}", stock.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Stock> stockList = stockRepository.findAll();
        assertThat(stockList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
