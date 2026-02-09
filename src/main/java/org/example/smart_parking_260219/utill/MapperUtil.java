package org.example.smart_parking_260219.utill;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

public enum MapperUtil {
    INSTANCE; // 상수 선언 - ModelMapper를 Singleton(싱글톤)으로 구현

    private final ModelMapper modelMapper;
    MapperUtil() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        // STRICT 업격모드, 필드명과 타입이 모두 일치해야 함
    }

    public ModelMapper getInstance() {
        return modelMapper;
    }
}
