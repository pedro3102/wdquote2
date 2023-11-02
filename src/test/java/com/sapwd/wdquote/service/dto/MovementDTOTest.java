package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementDTO.class);
        MovementDTO movementDTO1 = new MovementDTO();
        movementDTO1.setId(1L);
        MovementDTO movementDTO2 = new MovementDTO();
        assertThat(movementDTO1).isNotEqualTo(movementDTO2);
        movementDTO2.setId(movementDTO1.getId());
        assertThat(movementDTO1).isEqualTo(movementDTO2);
        movementDTO2.setId(2L);
        assertThat(movementDTO1).isNotEqualTo(movementDTO2);
        movementDTO1.setId(null);
        assertThat(movementDTO1).isNotEqualTo(movementDTO2);
    }
}
