package com.ucsmy.collection.services;

import com.ucsmy.collection.models.SpiderApplicaton;
import com.ucsmy.collection.repositories.SpiderApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/8.
 */
@Service
public class SpiderApplicationService {

    @Autowired
    private SpiderApplicationRepository spiderApplicationRepository;

    public Map findByVersionAfter (double version) {
        Map map = new HashMap();
        List<SpiderApplicaton> spiderApplicatons = spiderApplicationRepository.findByVersionAfterOrderByVersionDesc(version);
        if(spiderApplicatons!=null && spiderApplicatons.size() != 0 && spiderApplicatons.get(0).getVersion() > version) {
            map.put("success",true);
            map.put("url",spiderApplicatons.get(0).getUrl());
            map.put("name",spiderApplicatons.get(0).getName());
            map.put("info",spiderApplicatons.get(0).getInfo());
            map.put("version",spiderApplicatons.get(0).getVersion());
        } else {
            map.put("success",false);
            map.put("info","没有新版本");
        }
        return map;
    }

}
