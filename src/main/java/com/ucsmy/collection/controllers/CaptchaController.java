package com.ucsmy.collection.controllers;

import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Spider;
import com.ucsmy.collection.services.CaptchaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<Captcha> findAll() {
        return captchaService.findAll();
    }


}
