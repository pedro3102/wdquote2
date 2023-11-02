package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemModelTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemModel.class);
        SystemModel systemModel1 = new SystemModel();
        systemModel1.setId(1L);
        SystemModel systemModel2 = new SystemModel();
        systemModel2.setId(systemModel1.getId());
        assertThat(systemModel1).isEqualTo(systemModel2);
        systemModel2.setId(2L);
        assertThat(systemModel1).isNotEqualTo(systemModel2);
        systemModel1.setId(null);
        assertThat(systemModel1).isNotEqualTo(systemModel2);
    }
}
