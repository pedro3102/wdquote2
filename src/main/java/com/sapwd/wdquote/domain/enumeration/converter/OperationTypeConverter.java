package com.sapwd.wdquote.domain.enumeration.converter;

import com.sapwd.wdquote.domain.enumeration.OperationType;
import jakarta.persistence.AttributeConverter;

public class OperationTypeConverter implements AttributeConverter<OperationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperationType operationType) {
        return operationType.getId();
    }

    @Override
    public OperationType convertToEntityAttribute(Integer id) {
        return OperationType.getById(id);
    }
}
