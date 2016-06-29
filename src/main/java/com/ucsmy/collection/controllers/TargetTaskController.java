package com.ucsmy.collection.controllers;

import com.ucsmy.collection.dto.TargetTaskDTO;
import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.TargetTask;
import com.ucsmy.collection.services.TargetTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/targettask")
public class TargetTaskController {

    @Autowired
    private TargetTaskService targetTaskService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<TargetTaskDTO> findAll() {
        return targetTaskService.findAll();
    }
}
