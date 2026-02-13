package org.example.smart_parking_260219.service;

import lombok.extern.slf4j.Slf4j;
import org.example.smart_parking_260219.dao.SubscribeDAO;
import org.example.smart_parking_260219.dto.SubscribeDTO;
import org.example.smart_parking_260219.util.MapperUtil;
import org.example.smart_parking_260219.vo.SubscribeVO;
import org.modelmapper.ModelMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public enum SubscribeService {
    INSTANCE;

    private SubscribeDAO subscribeDAO;
    private ModelMapper modelMapper;

    SubscribeService() {
        subscribeDAO = new SubscribeDAO();
        modelMapper = MapperUtil.INSTANCE.getInstance();
    }

    public void addSubscribe(SubscribeDTO subscribeDTO) throws SQLException {
        SubscribeVO subscribeVO  = modelMapper.map(subscribeDTO, SubscribeVO.class);

        log.info(String.valueOf(subscribeVO));
        subscribeDAO.insertSubscribe(subscribeVO);
    }

    public List<SubscribeDTO> getAllSubscribe() throws SQLException {
        List<SubscribeVO> subscribeVOList = subscribeDAO.selectAllSubscribe();
        List<SubscribeDTO> subscribeDTOList = subscribeVOList.stream()
                .map(subscribeVO -> modelMapper.map(subscribeVO, SubscribeDTO.class)).toList();
        return subscribeDTOList;

    }

    }

    public void modifySubscribe(SubscribeDTO subscribeDTO) throws SQLException {
        SubscribeVO subscribeVO = modelMapper.map(subscribeDTO, SubscribeVO.class);
        subscribeDAO.updateSubscribe(subscribeVO);
    }

    }

        }
