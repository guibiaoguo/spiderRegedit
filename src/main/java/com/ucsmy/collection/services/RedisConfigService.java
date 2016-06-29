package com.ucsmy.collection.services;
import com.ucsmy.collection.models.Job;
import com.ucsmy.collection.models.RedisConfig;
import com.ucsmy.collection.repositories.RedisConfigRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */

@Service
public class RedisConfigService {

    @Autowired
    private RedisConfigRepository redisConfigRepository;

    private static final Logger logger = LoggerFactory.getLogger(RedisConfigService.class);

    public static final String CACHE_NAME = "cache.redisconfig";
    public static final String CACHE_NAME_redisconfig = "cache.redisconfig.all";

    public void save(RedisConfig redisConfig) {
        redisConfigRepository.save(redisConfig);
    }

    public List<RedisConfig> findAll() {
        return redisConfigRepository.findAll();
    }
}
