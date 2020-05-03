package com.menttech.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.menttech.mitienda.web.rest.TestUtil;

public class ShoppingCarDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingCarDTO.class);
        ShoppingCarDTO shoppingCarDTO1 = new ShoppingCarDTO();
        shoppingCarDTO1.setId(1L);
        ShoppingCarDTO shoppingCarDTO2 = new ShoppingCarDTO();
        assertThat(shoppingCarDTO1).isNotEqualTo(shoppingCarDTO2);
        shoppingCarDTO2.setId(shoppingCarDTO1.getId());
        assertThat(shoppingCarDTO1).isEqualTo(shoppingCarDTO2);
        shoppingCarDTO2.setId(2L);
        assertThat(shoppingCarDTO1).isNotEqualTo(shoppingCarDTO2);
        shoppingCarDTO1.setId(null);
        assertThat(shoppingCarDTO1).isNotEqualTo(shoppingCarDTO2);
    }
}
