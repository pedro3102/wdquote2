package com.sapwd.wdquote.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SystemaDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemaDTO.class);
        SystemaDTO systemaDTO1 = new SystemaDTO();
        systemaDTO1.setId(1L);
        SystemaDTO systemaDTO2 = new SystemaDTO();
        assertThat(systemaDTO1).isNotEqualTo(systemaDTO2);
        systemaDTO2.setId(systemaDTO1.getId());
        assertThat(systemaDTO1).isEqualTo(systemaDTO2);
        systemaDTO2.setId(2L);
        assertThat(systemaDTO1).isNotEqualTo(systemaDTO2);
        systemaDTO1.setId(null);
        assertThat(systemaDTO1).isNotEqualTo(systemaDTO2);
    }
}
