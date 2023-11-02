package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Systema.class);
        Systema systema1 = new Systema();
        systema1.setId(1L);
        Systema systema2 = new Systema();
        systema2.setId(systema1.getId());
        assertThat(systema1).isEqualTo(systema2);
        systema2.setId(2L);
        assertThat(systema1).isNotEqualTo(systema2);
        systema1.setId(null);
        assertThat(systema1).isNotEqualTo(systema2);
    }
}
