package com.menttech.mitienda.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.menttech.mitienda.web.rest.TestUtil;

public class SellerDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SellerDTO.class);
        SellerDTO sellerDTO1 = new SellerDTO();
        sellerDTO1.setId(1L);
        SellerDTO sellerDTO2 = new SellerDTO();
        assertThat(sellerDTO1).isNotEqualTo(sellerDTO2);
        sellerDTO2.setId(sellerDTO1.getId());
        assertThat(sellerDTO1).isEqualTo(sellerDTO2);
        sellerDTO2.setId(2L);
        assertThat(sellerDTO1).isNotEqualTo(sellerDTO2);
        sellerDTO1.setId(null);
        assertThat(sellerDTO1).isNotEqualTo(sellerDTO2);
    }
}
