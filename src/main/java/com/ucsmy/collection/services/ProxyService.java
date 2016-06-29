package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.ProxyDTO;
import com.ucsmy.collection.models.Proxy;
import com.ucsmy.collection.repositories.ProxyRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/18.
 */

@Service
public class ProxyService {

    @Autowired
    private ProxyRepository proxyRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.proxy";
    public static final String CACHE_NAME_PROXY = "cache.proxy.all";

    @Cacheable(value = CACHE_NAME_PROXY, key = "#root.targetClass + #root.methodName")
    public List<Proxy> findAll(){
        return proxyRepository.findAll();
    }

    public List<ProxyDTO> transform(List<Proxy> proxys) {
        List<ProxyDTO> proxyDTOs = new ArrayList<>();
        for (Proxy proxy : proxys) {
            ProxyDTO proxyDTO = new ProxyDTO();
            try {
                BeanUtils.copyProperties(proxyDTO, proxy);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            proxyDTOs.add(proxyDTO);
        }
        return proxyDTOs;
    }

    public List<Proxy> findFirst100ByStatus(int status) {
       return proxyRepository.findFirst100ByStatus(status);
    }

    public void save(Proxy proxy) {
        proxyRepository.save(proxy);
    }

    public void delete(Proxy proxy) {
        proxyRepository.delete(proxy);
    }
}
