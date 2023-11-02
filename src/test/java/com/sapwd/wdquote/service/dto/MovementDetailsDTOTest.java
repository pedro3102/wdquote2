package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementDetailsDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementDetailsDTO.class);
        MovementDetailsDTO movementDetailsDTO1 = new MovementDetailsDTO();
        movementDetailsDTO1.setId(1L);
        MovementDetailsDTO movementDetailsDTO2 = new MovementDetailsDTO();
        assertThat(movementDetailsDTO1).isNotEqualTo(movementDetailsDTO2);
        movementDetailsDTO2.setId(movementDetailsDTO1.getId());
        assertThat(movementDetailsDTO1).isEqualTo(movementDetailsDTO2);
        movementDetailsDTO2.setId(2L);
        assertThat(movementDetailsDTO1).isNotEqualTo(movementDetailsDTO2);
        movementDetailsDTO1.setId(null);
        assertThat(movementDetailsDTO1).isNotEqualTo(movementDetailsDTO2);
    }
}
