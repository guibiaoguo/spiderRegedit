package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.CookieDTO;
import com.ucsmy.collection.models.Cookie;
import com.ucsmy.collection.models.Job;
import com.ucsmy.collection.repositories.CookieRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */

@Service
public class CookieService {

    @Autowired
    private CookieRepository cookieRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.cookie";
    public static final String CACHE_NAME_COOKIE = "cache.cookie.all";

//    @Cacheable(value = CACHE_NAME_PROXY, key = "#root.targetClass + #root.methodName")
    public List<Cookie> findAll(){
        return cookieRepository.findAll();
    }

    public List<CookieDTO> transform(List<Cookie> cookies) {
        List<CookieDTO> cookieDTOs = new ArrayList<>();
        for (Cookie cookie : cookies) {
            CookieDTO cookieDTO = new CookieDTO();
            if(cookie.getStatus() == 0 ) {
                try {
                    BeanUtils.copyProperties(cookieDTO, cookie);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                cookieDTOs.add(cookieDTO);
            }
        }
        return cookieDTOs;
    }

    @CacheEvict(value = CACHE_NAME_COOKIE, allEntries = true)
    public void save(Cookie cookie) {
        cookieRepository.save(cookie);
    }

    @Cacheable(value = CACHE_NAME_COOKIE,key = "#root.targetClass + '.' + #root.methodName + #job.id")
    public List<CookieDTO> findByJobAndStatus(Job job, int status) {
        List<Cookie> cookies = cookieRepository.findByJobAndStatus(job,0);
        return transform(cookies);
    }

    public List<Cookie> findByStatus(int i) {
       return cookieRepository.findByStatus(i);
    }
}
