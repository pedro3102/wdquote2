package com.sapwd.wdquote.domain.enumeration.converter;

import com.sapwd.wdquote.domain.enumeration.OperationCounterpart;
import jakarta.persistence.AttributeConverter;

public class OperationCounterpartConverter implements AttributeConverter<OperationCounterpart, Integer> {

    @Override
    public Integer convertToDatabaseColumn(OperationCounterpart operationCounterpart) {
        return operationCounterpart == null ? null : operationCounterpart.getId();
    }

    @Override
    public OperationCounterpart convertToEntityAttribute(Integer id) {
        return OperationCounterpart.getById(id);
    }
}
