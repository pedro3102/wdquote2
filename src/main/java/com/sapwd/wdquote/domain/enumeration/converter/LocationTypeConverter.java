package com.sapwd.wdquote.domain.enumeration.converter;

import com.sapwd.wdquote.domain.enumeration.LocationType;
import jakarta.persistence.AttributeConverter;

public class LocationTypeConverter implements AttributeConverter<LocationType, Integer> {

    @Override
    public Integer convertToDatabaseColumn(LocationType locationType) {
        return locationType.getId();
    }

    @Override
    public LocationType convertToEntityAttribute(Integer id) {
        return LocationType.getById(id);
    }
}
