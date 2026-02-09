package org.example.smart_parking_260219.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;

/* 열겨형으로 작업하는 이유 -> enum 상수는 JVM 내에서 단 하나의 인스턴스만 생성 (=싱글턴) */
public enum MapperUtil {
    /*
    # Why enum?
    - JVM이 enum의 인스턴스가 딱 하나만 존재하도록 완벽하게 보장함.
    - 역직렬화(객체를 파일로 저장했다가 다시 불러올 때) 새로운 객체가 생기는 것을 방지함.
    - getInstance() 로직을 복잡하게 짤 필요 없이, INSTANCE; 한줄이면 끝
     */

    /*
    # MapperUtil?
    - 이름이 같은 필드끼리 데이터를 알아서 복사해주는 '자동 복사기'
    - 이 복사기를 설정하는 코드가 중복되지 않도록 util에 몰아넣고 공용으로 사용하는 것.
    - MapperUtil의 생성자 안에서 이루어지는 설정들은 "어떻게 하면 실수 없이 데이터를 잘 복사할까?"에 대한 규칙들

    # ModelMapper : DTO, VO 사이의 데이터 복사 작업을 대신해주는 비서 역할.
     */

    INSTANCE;

    private final ModelMapper modelMapper;
    MapperUtil() {
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                // 기본적으로 자바는 getter/setter를 통해 값을 가져오지만, 이 설정을 켜면 필드(변수) 이름만 보고도 직접 매핑을 시도
                .setFieldMatchingEnabled(true)
                // 보통 VO, DTO의 필드는 private로 보호되어 있음.
                // 이 설정은 ModelMapper가 private 변수에도 접근해서 값을 넣고 뺄수있게 허용
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
                // 엄격모드는, title이라는 값을 subtitle에 대충 비슷하다고 넣어버리는 참사를 방지함.
                .setMatchingStrategy(MatchingStrategies.STRICT);
        // STRICT : 엄격모드. 필드명과 타입이 모두 일치해야 매핑
    }
    public ModelMapper getInstance() {
        return modelMapper;
    }
}
