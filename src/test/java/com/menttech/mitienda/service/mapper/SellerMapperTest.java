package com.menttech.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class SellerMapperTest {

    private SellerMapper sellerMapper;

    @BeforeEach
    public void setUp() {
        sellerMapper = new SellerMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(sellerMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(sellerMapper.fromId(null)).isNull();
    }
}
