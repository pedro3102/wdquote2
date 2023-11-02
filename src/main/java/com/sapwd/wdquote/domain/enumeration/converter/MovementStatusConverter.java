package com.sapwd.wdquote.domain.enumeration.converter;

import com.sapwd.wdquote.domain.enumeration.MovementStatus;
import jakarta.persistence.AttributeConverter;

public class MovementStatusConverter implements AttributeConverter<MovementStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(MovementStatus movementStatus) {
        return movementStatus.getId();
    }

    @Override
    public MovementStatus convertToEntityAttribute(Integer id) {
        return MovementStatus.getById(id);
    }
}
