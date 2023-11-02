package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class MovementTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Movement.class);
        Movement movement1 = new Movement();
        movement1.setId(1L);
        Movement movement2 = new Movement();
        movement2.setId(movement1.getId());
        assertThat(movement1).isEqualTo(movement2);
        movement2.setId(2L);
        assertThat(movement1).isNotEqualTo(movement2);
        movement1.setId(null);
        assertThat(movement1).isNotEqualTo(movement2);
    }
}
