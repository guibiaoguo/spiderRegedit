package com.ucsmy.collection.services;

import com.ucsmy.collection.models.SpiderJob;
import com.ucsmy.collection.models.SpiderLog;
import com.ucsmy.collection.repositories.SpiderJobRepository;
import com.ucsmy.collection.repositories.SpiderLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/12/29.
 */
@Service
public class SpiderLogService {
    @Autowired
    private SpiderLogRepository spiderLogRepository;

    public void save(SpiderLog spiderLog) {
        spiderLogRepository.save(spiderLog);
    }
}
