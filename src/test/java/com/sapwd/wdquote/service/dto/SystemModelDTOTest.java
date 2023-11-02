package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemModelDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemModelDTO.class);
        SystemModelDTO systemModelDTO1 = new SystemModelDTO();
        systemModelDTO1.setId(1L);
        SystemModelDTO systemModelDTO2 = new SystemModelDTO();
        assertThat(systemModelDTO1).isNotEqualTo(systemModelDTO2);
        systemModelDTO2.setId(systemModelDTO1.getId());
        assertThat(systemModelDTO1).isEqualTo(systemModelDTO2);
        systemModelDTO2.setId(2L);
        assertThat(systemModelDTO1).isNotEqualTo(systemModelDTO2);
        systemModelDTO1.setId(null);
        assertThat(systemModelDTO1).isNotEqualTo(systemModelDTO2);
    }
}
