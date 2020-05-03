package com.menttech.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.menttech.mitienda.web.rest.TestUtil;

public class HomeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomeDTO.class);
        HomeDTO homeDTO1 = new HomeDTO();
        homeDTO1.setId(1L);
        HomeDTO homeDTO2 = new HomeDTO();
        assertThat(homeDTO1).isNotEqualTo(homeDTO2);
        homeDTO2.setId(homeDTO1.getId());
        assertThat(homeDTO1).isEqualTo(homeDTO2);
        homeDTO2.setId(2L);
        assertThat(homeDTO1).isNotEqualTo(homeDTO2);
        homeDTO1.setId(null);
        assertThat(homeDTO1).isNotEqualTo(homeDTO2);
    }
}
