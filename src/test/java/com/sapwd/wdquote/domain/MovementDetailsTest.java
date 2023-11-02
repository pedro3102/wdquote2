package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MovementDetails.class);
        MovementDetails movementDetails1 = new MovementDetails();
        movementDetails1.setId(1L);
        MovementDetails movementDetails2 = new MovementDetails();
        movementDetails2.setId(movementDetails1.getId());
        assertThat(movementDetails1).isEqualTo(movementDetails2);
        movementDetails2.setId(2L);
        assertThat(movementDetails1).isNotEqualTo(movementDetails2);
        movementDetails1.setId(null);
        assertThat(movementDetails1).isNotEqualTo(movementDetails2);
    }
}
