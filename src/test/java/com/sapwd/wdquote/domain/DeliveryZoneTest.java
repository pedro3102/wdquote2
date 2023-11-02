package com.sapwd.wdquote.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.sapwd.wdquote.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DeliveryZoneTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeliveryZone.class);
        DeliveryZone deliveryZone1 = new DeliveryZone();
        deliveryZone1.setId(1L);
        DeliveryZone deliveryZone2 = new DeliveryZone();
        deliveryZone2.setId(deliveryZone1.getId());
        assertThat(deliveryZone1).isEqualTo(deliveryZone2);
        deliveryZone2.setId(2L);
        assertThat(deliveryZone1).isNotEqualTo(deliveryZone2);
        deliveryZone1.setId(null);
        assertThat(deliveryZone1).isNotEqualTo(deliveryZone2);
    }
}
