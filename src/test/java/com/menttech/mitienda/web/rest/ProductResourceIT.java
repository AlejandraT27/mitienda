package com.menttech.mitienda.web.rest;

import com.menttech.mitienda.MitiendaApp;
import com.menttech.mitienda.domain.Product;
import com.menttech.mitienda.domain.Stock;
import com.menttech.mitienda.domain.Customer;
import com.menttech.mitienda.domain.ProductCategory;
import com.menttech.mitienda.repository.ProductRepository;
import com.menttech.mitienda.service.ProductService;
import com.menttech.mitienda.service.dto.ProductDTO;
import com.menttech.mitienda.service.mapper.ProductMapper;
import com.menttech.mitienda.service.dto.ProductCriteria;
import com.menttech.mitienda.service.ProductQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.menttech.mitienda.domain.enumeration.Size;
import com.menttech.mitienda.domain.enumeration.Color;
/**
 * Integration tests for the {@link ProductResource} REST controller.
 */
@SpringBootTest(classes = MitiendaApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ProductResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PURCHASE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PURCHASE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PURCHASE_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SALE_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALE_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALE_PRICE = new BigDecimal(1 - 1);

    private static final Size DEFAULT_SIZE = Size.XS;
    private static final Size UPDATED_SIZE = Size.S;

    private static final Color DEFAULT_COLOR = Color.Blanco;
    private static final Color UPDATED_COLOR = Color.Cafe;

    private static final String DEFAULT_STOCK = "AAAAAAAAAA";
    private static final String UPDATED_STOCK = "BBBBBBBBBB";

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductQueryService productQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductMockMvc;

    private Product product;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createEntity(EntityManager em) {
        Product product = new Product()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .purchasePrice(DEFAULT_PURCHASE_PRICE)
            .salePrice(DEFAULT_SALE_PRICE)
            .size(DEFAULT_SIZE)
            .color(DEFAULT_COLOR)
            .stock(DEFAULT_STOCK)
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE);
        return product;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Product createUpdatedEntity(EntityManager em) {
        Product product = new Product()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .size(UPDATED_SIZE)
            .color(UPDATED_COLOR)
            .stock(UPDATED_STOCK)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        return product;
    }

    @BeforeEach
    public void initTest() {
        product = createEntity(em);
    }

    @Test
    @Transactional
    public void createProduct() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isCreated());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate + 1);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProduct.getPurchasePrice()).isEqualTo(DEFAULT_PURCHASE_PRICE);
        assertThat(testProduct.getSalePrice()).isEqualTo(DEFAULT_SALE_PRICE);
        assertThat(testProduct.getSize()).isEqualTo(DEFAULT_SIZE);
        assertThat(testProduct.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testProduct.getStock()).isEqualTo(DEFAULT_STOCK);
        assertThat(testProduct.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createProductWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productRepository.findAll().size();

        // Create the Product with an existing ID
        product.setId(1L);
        ProductDTO productDTO = productMapper.toDto(product);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setName(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSizeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setSize(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkColorIsRequired() throws Exception {
        int databaseSizeBeforeTest = productRepository.findAll().size();
        // set the field null
        product.setColor(null);

        // Create the Product, which fails.
        ProductDTO productDTO = productMapper.toDto(product);

        restProductMockMvc.perform(post("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProducts() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList
        restProductMockMvc.perform(get("/api/products?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));
    }
    
    @Test
    @Transactional
    public void getProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", product.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(product.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.purchasePrice").value(DEFAULT_PURCHASE_PRICE.intValue()))
            .andExpect(jsonPath("$.salePrice").value(DEFAULT_SALE_PRICE.intValue()))
            .andExpect(jsonPath("$.size").value(DEFAULT_SIZE.toString()))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR.toString()))
            .andExpect(jsonPath("$.stock").value(DEFAULT_STOCK))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)));
    }


    @Test
    @Transactional
    public void getProductsByIdFiltering() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        Long id = product.getId();

        defaultProductShouldBeFound("id.equals=" + id);
        defaultProductShouldNotBeFound("id.notEquals=" + id);

        defaultProductShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.greaterThan=" + id);

        defaultProductShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProductShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProductsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name equals to DEFAULT_NAME
        defaultProductShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name not equals to DEFAULT_NAME
        defaultProductShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the productList where name not equals to UPDATED_NAME
        defaultProductShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProductShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the productList where name equals to UPDATED_NAME
        defaultProductShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name is not null
        defaultProductShouldBeFound("name.specified=true");

        // Get all the productList where name is null
        defaultProductShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByNameContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name contains DEFAULT_NAME
        defaultProductShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the productList where name contains UPDATED_NAME
        defaultProductShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProductsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where name does not contain DEFAULT_NAME
        defaultProductShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the productList where name does not contain UPDATED_NAME
        defaultProductShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProductsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description equals to DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description not equals to DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description not equals to UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the productList where description equals to UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description is not null
        defaultProductShouldBeFound("description.specified=true");

        // Get all the productList where description is null
        defaultProductShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description contains DEFAULT_DESCRIPTION
        defaultProductShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description contains UPDATED_DESCRIPTION
        defaultProductShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProductsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where description does not contain DEFAULT_DESCRIPTION
        defaultProductShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the productList where description does not contain UPDATED_DESCRIPTION
        defaultProductShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice equals to DEFAULT_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.equals=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice equals to UPDATED_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.equals=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice not equals to DEFAULT_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.notEquals=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice not equals to UPDATED_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.notEquals=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice in DEFAULT_PURCHASE_PRICE or UPDATED_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.in=" + DEFAULT_PURCHASE_PRICE + "," + UPDATED_PURCHASE_PRICE);

        // Get all the productList where purchasePrice equals to UPDATED_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.in=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is not null
        defaultProductShouldBeFound("purchasePrice.specified=true");

        // Get all the productList where purchasePrice is null
        defaultProductShouldNotBeFound("purchasePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is greater than or equal to DEFAULT_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.greaterThanOrEqual=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice is greater than or equal to UPDATED_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.greaterThanOrEqual=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is less than or equal to DEFAULT_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.lessThanOrEqual=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice is less than or equal to SMALLER_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.lessThanOrEqual=" + SMALLER_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is less than DEFAULT_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.lessThan=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice is less than UPDATED_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.lessThan=" + UPDATED_PURCHASE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsByPurchasePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where purchasePrice is greater than DEFAULT_PURCHASE_PRICE
        defaultProductShouldNotBeFound("purchasePrice.greaterThan=" + DEFAULT_PURCHASE_PRICE);

        // Get all the productList where purchasePrice is greater than SMALLER_PURCHASE_PRICE
        defaultProductShouldBeFound("purchasePrice.greaterThan=" + SMALLER_PURCHASE_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsBySalePriceIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice equals to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.equals=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.equals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice not equals to DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.notEquals=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice not equals to UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.notEquals=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice in DEFAULT_SALE_PRICE or UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.in=" + DEFAULT_SALE_PRICE + "," + UPDATED_SALE_PRICE);

        // Get all the productList where salePrice equals to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.in=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is not null
        defaultProductShouldBeFound("salePrice.specified=true");

        // Get all the productList where salePrice is null
        defaultProductShouldNotBeFound("salePrice.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is greater than or equal to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.greaterThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is greater than or equal to UPDATED_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.greaterThanOrEqual=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is less than or equal to DEFAULT_SALE_PRICE
        defaultProductShouldBeFound("salePrice.lessThanOrEqual=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is less than or equal to SMALLER_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.lessThanOrEqual=" + SMALLER_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsLessThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is less than DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.lessThan=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is less than UPDATED_SALE_PRICE
        defaultProductShouldBeFound("salePrice.lessThan=" + UPDATED_SALE_PRICE);
    }

    @Test
    @Transactional
    public void getAllProductsBySalePriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where salePrice is greater than DEFAULT_SALE_PRICE
        defaultProductShouldNotBeFound("salePrice.greaterThan=" + DEFAULT_SALE_PRICE);

        // Get all the productList where salePrice is greater than SMALLER_SALE_PRICE
        defaultProductShouldBeFound("salePrice.greaterThan=" + SMALLER_SALE_PRICE);
    }


    @Test
    @Transactional
    public void getAllProductsBySizeIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where size equals to DEFAULT_SIZE
        defaultProductShouldBeFound("size.equals=" + DEFAULT_SIZE);

        // Get all the productList where size equals to UPDATED_SIZE
        defaultProductShouldNotBeFound("size.equals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllProductsBySizeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where size not equals to DEFAULT_SIZE
        defaultProductShouldNotBeFound("size.notEquals=" + DEFAULT_SIZE);

        // Get all the productList where size not equals to UPDATED_SIZE
        defaultProductShouldBeFound("size.notEquals=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllProductsBySizeIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where size in DEFAULT_SIZE or UPDATED_SIZE
        defaultProductShouldBeFound("size.in=" + DEFAULT_SIZE + "," + UPDATED_SIZE);

        // Get all the productList where size equals to UPDATED_SIZE
        defaultProductShouldNotBeFound("size.in=" + UPDATED_SIZE);
    }

    @Test
    @Transactional
    public void getAllProductsBySizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where size is not null
        defaultProductShouldBeFound("size.specified=true");

        // Get all the productList where size is null
        defaultProductShouldNotBeFound("size.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color equals to DEFAULT_COLOR
        defaultProductShouldBeFound("color.equals=" + DEFAULT_COLOR);

        // Get all the productList where color equals to UPDATED_COLOR
        defaultProductShouldNotBeFound("color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllProductsByColorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color not equals to DEFAULT_COLOR
        defaultProductShouldNotBeFound("color.notEquals=" + DEFAULT_COLOR);

        // Get all the productList where color not equals to UPDATED_COLOR
        defaultProductShouldBeFound("color.notEquals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllProductsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color in DEFAULT_COLOR or UPDATED_COLOR
        defaultProductShouldBeFound("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR);

        // Get all the productList where color equals to UPDATED_COLOR
        defaultProductShouldNotBeFound("color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    public void getAllProductsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where color is not null
        defaultProductShouldBeFound("color.specified=true");

        // Get all the productList where color is null
        defaultProductShouldNotBeFound("color.specified=false");
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock equals to DEFAULT_STOCK
        defaultProductShouldBeFound("stock.equals=" + DEFAULT_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.equals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsNotEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock not equals to DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.notEquals=" + DEFAULT_STOCK);

        // Get all the productList where stock not equals to UPDATED_STOCK
        defaultProductShouldBeFound("stock.notEquals=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsInShouldWork() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock in DEFAULT_STOCK or UPDATED_STOCK
        defaultProductShouldBeFound("stock.in=" + DEFAULT_STOCK + "," + UPDATED_STOCK);

        // Get all the productList where stock equals to UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.in=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockIsNullOrNotNull() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock is not null
        defaultProductShouldBeFound("stock.specified=true");

        // Get all the productList where stock is null
        defaultProductShouldNotBeFound("stock.specified=false");
    }
                @Test
    @Transactional
    public void getAllProductsByStockContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock contains DEFAULT_STOCK
        defaultProductShouldBeFound("stock.contains=" + DEFAULT_STOCK);

        // Get all the productList where stock contains UPDATED_STOCK
        defaultProductShouldNotBeFound("stock.contains=" + UPDATED_STOCK);
    }

    @Test
    @Transactional
    public void getAllProductsByStockNotContainsSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        // Get all the productList where stock does not contain DEFAULT_STOCK
        defaultProductShouldNotBeFound("stock.doesNotContain=" + DEFAULT_STOCK);

        // Get all the productList where stock does not contain UPDATED_STOCK
        defaultProductShouldBeFound("stock.doesNotContain=" + UPDATED_STOCK);
    }


    @Test
    @Transactional
    public void getAllProductsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Stock user = StockResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        product.setUser(user);
        productRepository.saveAndFlush(product);
        Long userId = user.getId();

        // Get all the productList where user equals to userId
        defaultProductShouldBeFound("userId.equals=" + userId);

        // Get all the productList where user equals to userId + 1
        defaultProductShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByProductCategoyIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        Customer productCategoy = CustomerResourceIT.createEntity(em);
        em.persist(productCategoy);
        em.flush();
        product.addProductCategoy(productCategoy);
        productRepository.saveAndFlush(product);
        Long productCategoyId = productCategoy.getId();

        // Get all the productList where productCategoy equals to productCategoyId
        defaultProductShouldBeFound("productCategoyId.equals=" + productCategoyId);

        // Get all the productList where productCategoy equals to productCategoyId + 1
        defaultProductShouldNotBeFound("productCategoyId.equals=" + (productCategoyId + 1));
    }


    @Test
    @Transactional
    public void getAllProductsByProductCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);
        ProductCategory productCategory = ProductCategoryResourceIT.createEntity(em);
        em.persist(productCategory);
        em.flush();
        product.addProductCategory(productCategory);
        productRepository.saveAndFlush(product);
        Long productCategoryId = productCategory.getId();

        // Get all the productList where productCategory equals to productCategoryId
        defaultProductShouldBeFound("productCategoryId.equals=" + productCategoryId);

        // Get all the productList where productCategory equals to productCategoryId + 1
        defaultProductShouldNotBeFound("productCategoryId.equals=" + (productCategoryId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProductShouldBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(product.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].purchasePrice").value(hasItem(DEFAULT_PURCHASE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].salePrice").value(hasItem(DEFAULT_SALE_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].size").value(hasItem(DEFAULT_SIZE.toString())))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR.toString())))
            .andExpect(jsonPath("$.[*].stock").value(hasItem(DEFAULT_STOCK)))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))));

        // Check, that the count call also returns 1
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProductShouldNotBeFound(String filter) throws Exception {
        restProductMockMvc.perform(get("/api/products?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProductMockMvc.perform(get("/api/products/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProduct() throws Exception {
        // Get the product
        restProductMockMvc.perform(get("/api/products/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Update the product
        Product updatedProduct = productRepository.findById(product.getId()).get();
        // Disconnect from session so that the updates on updatedProduct are not directly saved in db
        em.detach(updatedProduct);
        updatedProduct
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .purchasePrice(UPDATED_PURCHASE_PRICE)
            .salePrice(UPDATED_SALE_PRICE)
            .size(UPDATED_SIZE)
            .color(UPDATED_COLOR)
            .stock(UPDATED_STOCK)
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE);
        ProductDTO productDTO = productMapper.toDto(updatedProduct);

        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isOk());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
        Product testProduct = productList.get(productList.size() - 1);
        assertThat(testProduct.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProduct.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProduct.getPurchasePrice()).isEqualTo(UPDATED_PURCHASE_PRICE);
        assertThat(testProduct.getSalePrice()).isEqualTo(UPDATED_SALE_PRICE);
        assertThat(testProduct.getSize()).isEqualTo(UPDATED_SIZE);
        assertThat(testProduct.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testProduct.getStock()).isEqualTo(UPDATED_STOCK);
        assertThat(testProduct.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testProduct.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingProduct() throws Exception {
        int databaseSizeBeforeUpdate = productRepository.findAll().size();

        // Create the Product
        ProductDTO productDTO = productMapper.toDto(product);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductMockMvc.perform(put("/api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(productDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Product in the database
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProduct() throws Exception {
        // Initialize the database
        productRepository.saveAndFlush(product);

        int databaseSizeBeforeDelete = productRepository.findAll().size();

        // Delete the product
        restProductMockMvc.perform(delete("/api/products/{id}", product.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Product> productList = productRepository.findAll();
        assertThat(productList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
