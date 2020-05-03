package com.menttech.mitienda.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.menttech.mitienda.web.rest.TestUtil;

public class ShoppingCarTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ShoppingCar.class);
        ShoppingCar shoppingCar1 = new ShoppingCar();
        shoppingCar1.setId(1L);
        ShoppingCar shoppingCar2 = new ShoppingCar();
        shoppingCar2.setId(shoppingCar1.getId());
        assertThat(shoppingCar1).isEqualTo(shoppingCar2);
        shoppingCar2.setId(2L);
        assertThat(shoppingCar1).isNotEqualTo(shoppingCar2);
        shoppingCar1.setId(null);
        assertThat(shoppingCar1).isNotEqualTo(shoppingCar2);
    }
}
