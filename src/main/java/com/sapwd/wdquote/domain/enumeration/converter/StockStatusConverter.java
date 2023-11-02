package com.sapwd.wdquote.domain.enumeration.converter;

import com.sapwd.wdquote.domain.enumeration.StockStatus;
import jakarta.persistence.AttributeConverter;

public class StockStatusConverter implements AttributeConverter<StockStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(StockStatus operationType) {
        return operationType.getId();
    }

    @Override
    public StockStatus convertToEntityAttribute(Integer id) {
        return StockStatus.getById(id);
    }
}
