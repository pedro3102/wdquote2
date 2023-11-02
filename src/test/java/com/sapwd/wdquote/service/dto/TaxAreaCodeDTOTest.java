package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAreaCodeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAreaCodeDTO.class);
        TaxAreaCodeDTO taxAreaCodeDTO1 = new TaxAreaCodeDTO();
        taxAreaCodeDTO1.setId(1L);
        TaxAreaCodeDTO taxAreaCodeDTO2 = new TaxAreaCodeDTO();
        assertThat(taxAreaCodeDTO1).isNotEqualTo(taxAreaCodeDTO2);
        taxAreaCodeDTO2.setId(taxAreaCodeDTO1.getId());
        assertThat(taxAreaCodeDTO1).isEqualTo(taxAreaCodeDTO2);
        taxAreaCodeDTO2.setId(2L);
        assertThat(taxAreaCodeDTO1).isNotEqualTo(taxAreaCodeDTO2);
        taxAreaCodeDTO1.setId(null);
        assertThat(taxAreaCodeDTO1).isNotEqualTo(taxAreaCodeDTO2);
    }
}
