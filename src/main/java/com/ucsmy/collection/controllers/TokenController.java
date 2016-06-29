package com.ucsmy.collection.controllers;

import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Token;
import com.ucsmy.collection.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/token")
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<Token> findAll() {
        return tokenService.findAll();
    }

}
