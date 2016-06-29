package com.ucsmy.collection.controllers;

import com.ucsmy.collection.dto.HelpTaskDTO;
import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.HelpTask;
import com.ucsmy.collection.services.HelpTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/helptask")
public class HelpTaskController {

    @Autowired
    private HelpTaskService helpTaskService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<HelpTaskDTO> findAll() {
        return helpTaskService.findAll();
    }

}
