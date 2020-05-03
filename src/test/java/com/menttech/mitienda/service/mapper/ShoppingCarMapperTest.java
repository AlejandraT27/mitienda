package com.menttech.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ShoppingCarMapperTest {

    private ShoppingCarMapper shoppingCarMapper;

    @BeforeEach
    public void setUp() {
        shoppingCarMapper = new ShoppingCarMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(shoppingCarMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(shoppingCarMapper.fromId(null)).isNull();
    }
}
