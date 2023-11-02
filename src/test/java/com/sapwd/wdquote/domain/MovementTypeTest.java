package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementType.class);
        MovementType movementType1 = new MovementType();
        movementType1.setId(1L);
        MovementType movementType2 = new MovementType();
        movementType2.setId(movementType1.getId());
        assertThat(movementType1).isEqualTo(movementType2);
        movementType2.setId(2L);
        assertThat(movementType1).isNotEqualTo(movementType2);
        movementType1.setId(null);
        assertThat(movementType1).isNotEqualTo(movementType2);
    }
}
