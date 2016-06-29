package com.ucsmy.collection.controllers;

import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Proxy;
import com.ucsmy.collection.services.ProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/proxy")
public class ProxyController {

    @Autowired
    private ProxyService proxyService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<Proxy> findAll() {
        return proxyService.findAll();
    }
}
