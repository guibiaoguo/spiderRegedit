package com.ucsmy.collection.services;

import com.ucsmy.collection.models.Spider;
import com.ucsmy.collection.models.SpiderJob;
import com.ucsmy.collection.repositories.SpiderJobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2015/12/7.
 */
@Service
public class SpiderJobService {

    @Autowired
    private SpiderJobRepository spiderJobRepository;

    public void save(SpiderJob spiderJob) {
        spiderJobRepository.save(spiderJob);
    }
}
