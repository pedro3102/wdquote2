package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UnitOfMeasureDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UnitOfMeasureDTO.class);
        UnitOfMeasureDTO unitOfMeasureDTO1 = new UnitOfMeasureDTO();
        unitOfMeasureDTO1.setId(1L);
        UnitOfMeasureDTO unitOfMeasureDTO2 = new UnitOfMeasureDTO();
        assertThat(unitOfMeasureDTO1).isNotEqualTo(unitOfMeasureDTO2);
        unitOfMeasureDTO2.setId(unitOfMeasureDTO1.getId());
        assertThat(unitOfMeasureDTO1).isEqualTo(unitOfMeasureDTO2);
        unitOfMeasureDTO2.setId(2L);
        assertThat(unitOfMeasureDTO1).isNotEqualTo(unitOfMeasureDTO2);
        unitOfMeasureDTO1.setId(null);
        assertThat(unitOfMeasureDTO1).isNotEqualTo(unitOfMeasureDTO2);
    }
}
