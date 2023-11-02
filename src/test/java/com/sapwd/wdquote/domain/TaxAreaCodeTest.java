package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TaxAreaCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TaxAreaCode.class);
        TaxAreaCode taxAreaCode1 = new TaxAreaCode();
        taxAreaCode1.setId(1L);
        TaxAreaCode taxAreaCode2 = new TaxAreaCode();
        taxAreaCode2.setId(taxAreaCode1.getId());
        assertThat(taxAreaCode1).isEqualTo(taxAreaCode2);
        taxAreaCode2.setId(2L);
        assertThat(taxAreaCode1).isNotEqualTo(taxAreaCode2);
        taxAreaCode1.setId(null);
        assertThat(taxAreaCode1).isNotEqualTo(taxAreaCode2);
    }
}
