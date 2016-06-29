package com.ucsmy.collection.services;

import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Spider;
import com.ucsmy.collection.repositories.CaptchaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Service
public class CaptchaService {

    @Autowired
    private CaptchaRepository captchaRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.captcha";
    public static final String CACHE_NAME_CAPTCHA = "cache.captcha.all";

    @Cacheable(value = CACHE_NAME_CAPTCHA,keyGenerator = "wiselyKeyGenerator")
    public List<Captcha> findAll(){
        return captchaRepository.findAll();
    }

}
