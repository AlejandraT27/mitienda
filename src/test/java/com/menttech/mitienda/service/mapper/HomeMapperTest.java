package com.menttech.mitienda.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HomeMapperTest {

    private HomeMapper homeMapper;

    @BeforeEach
    public void setUp() {
        homeMapper = new HomeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(homeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(homeMapper.fromId(null)).isNull();
    }
}
