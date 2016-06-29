package com.ucsmy.collection.services;

import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Token;
import com.ucsmy.collection.repositories.TokenRepository;
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
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.token";
    public static final String CACHE_NAME_TOKEN = "cache.token.all";

    @Cacheable(value = CACHE_NAME_TOKEN, key = "#root.targetClass + #root.methodName")
    public List<Token> findAll(){
        return tokenRepository.findAll();
    }
}
