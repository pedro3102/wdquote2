package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasureConversionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasureConversion.class);
        UnitOfMeasureConversion unitOfMeasureConversion1 = new UnitOfMeasureConversion();
        unitOfMeasureConversion1.setId(1L);
        UnitOfMeasureConversion unitOfMeasureConversion2 = new UnitOfMeasureConversion();
        unitOfMeasureConversion2.setId(unitOfMeasureConversion1.getId());
        assertThat(unitOfMeasureConversion1).isEqualTo(unitOfMeasureConversion2);
        unitOfMeasureConversion2.setId(2L);
        assertThat(unitOfMeasureConversion1).isNotEqualTo(unitOfMeasureConversion2);
        unitOfMeasureConversion1.setId(null);
        assertThat(unitOfMeasureConversion1).isNotEqualTo(unitOfMeasureConversion2);
    }
}
