package com.ucsmy.collection.controllers;

import com.ucsmy.collection.dto.JobDTO;
import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.Job;
import com.ucsmy.collection.services.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/18.
 */
@RestController
@RequestMapping("/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @Autowired
    private RedisTemplate redisTemplate;

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<Job> findAll() {
        return jobService.findJobByStatus(1);
    }

    @RequestMapping("/listAjax2")
    public List<JobDTO> findAll2() {
        return jobService.findAllByStatus(0);
    }

    @ResponseBody
    @RequestMapping("/spider")
    public JobDTO getJob() {
        List<JobDTO> jobDTOs = jobService.findAllByStatus(1);
        JobDTO jobDTO = jobDTOs.get(0);
        jobDTOs.remove(jobDTO);
        if(jobDTOs.size() == 0 )
            redisTemplate.delete("com.ucsmy.collection.services.JobServicefindAllByStatus0");
        else
            redisTemplate.opsForValue().set("com.ucsmy.collection.services.JobServicefindAllByStatus0",jobDTOs);
        return  jobDTO;
    }

    @RequestMapping("/spider/{uuid}")
    public JobDTO getJob(@PathVariable String uuid) {
        List<JobDTO> jobDTOs = jobService.findAllByStatus(1);
        JobDTO jobDTO = jobService.pop(uuid,jobDTOs);
        return jobDTO;
    }
}
