package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasureConversionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasureConversionDTO.class);
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO1 = new UnitOfMeasureConversionDTO();
        unitOfMeasureConversionDTO1.setId(1L);
        UnitOfMeasureConversionDTO unitOfMeasureConversionDTO2 = new UnitOfMeasureConversionDTO();
        assertThat(unitOfMeasureConversionDTO1).isNotEqualTo(unitOfMeasureConversionDTO2);
        unitOfMeasureConversionDTO2.setId(unitOfMeasureConversionDTO1.getId());
        assertThat(unitOfMeasureConversionDTO1).isEqualTo(unitOfMeasureConversionDTO2);
        unitOfMeasureConversionDTO2.setId(2L);
        assertThat(unitOfMeasureConversionDTO1).isNotEqualTo(unitOfMeasureConversionDTO2);
        unitOfMeasureConversionDTO1.setId(null);
        assertThat(unitOfMeasureConversionDTO1).isNotEqualTo(unitOfMeasureConversionDTO2);
    }
}
