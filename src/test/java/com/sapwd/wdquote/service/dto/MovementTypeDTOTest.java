package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementTypeDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementTypeDTO.class);
        MovementTypeDTO movementTypeDTO1 = new MovementTypeDTO();
        movementTypeDTO1.setId(1L);
        MovementTypeDTO movementTypeDTO2 = new MovementTypeDTO();
        assertThat(movementTypeDTO1).isNotEqualTo(movementTypeDTO2);
        movementTypeDTO2.setId(movementTypeDTO1.getId());
        assertThat(movementTypeDTO1).isEqualTo(movementTypeDTO2);
        movementTypeDTO2.setId(2L);
        assertThat(movementTypeDTO1).isNotEqualTo(movementTypeDTO2);
        movementTypeDTO1.setId(null);
        assertThat(movementTypeDTO1).isNotEqualTo(movementTypeDTO2);
    }
}
