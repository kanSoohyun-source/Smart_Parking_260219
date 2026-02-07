package org.example.smart_parking_260219.service;

import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

public enum MapperUtil {
    INSTANCE;

    private final org.modelmapper.ModelMapper modelMapper;
    MapperUtil() {
        modelMapper = new org.modelmapper.ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        // STRICT: 엄격모드. 필드명과 타입이 모두 일치해야 매핑
    }
    public org.modelmapper.ModelMapper getInstance() {
        return modelMapper;
    }
}
