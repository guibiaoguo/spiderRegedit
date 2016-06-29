package com.ucsmy.collection.services;

import com.ucsmy.collection.models.DataMsg;
import com.ucsmy.collection.repositories.DataMsgRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */

@Service
public class DataMsgService {

    @Autowired
    private DataMsgRepository dataMsgRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<DataMsg> findByStatus(int status) {
        return dataMsgRepository.findAll();
    }


    public DataMsg findByNameAndStatusOrderByIdDesc(String key,Boolean status) {
        List<DataMsg> dataMsgs = dataMsgRepository.findByNameAndStatusOrderByIdDesc(key,status);
        DataMsg dataMsg = null;
        if (dataMsgs != null && dataMsgs.size() > 1) {
            dataMsg = dataMsgs.get(0);
        }
        return dataMsg;
    }

    public void save(DataMsg dataMsg) {
        dataMsgRepository.save(dataMsg);
    }
}
