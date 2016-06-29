package com.ucsmy.collection.controllers;

import com.ucsmy.collection.dto.TasksDTO;
import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Tasks;
import com.ucsmy.collection.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Controller
@RequestMapping("/tasks")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<TasksDTO> findAll() {
        return tasksService.findAll();
    }
}
