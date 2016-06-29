package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.HelpTaskDTO;
import com.ucsmy.collection.models.HelpTask;
import com.ucsmy.collection.repositories.HelpTaskRepository;
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
public class HelpTaskService {

    @Autowired
    private HelpTaskRepository helpTaskRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.helpTask";
    public static final String CACHE_NAME_HELP_TASK = "cache.helpTask.all";

    @Cacheable(value = CACHE_NAME_HELP_TASK, key = "#root.targetClass + #root.methodName")
    public List<HelpTaskDTO> findAll() {
        System.out.println("我执行了sql语句");
        List<HelpTask> helpTasks = helpTaskRepository.findAll();
        List<HelpTaskDTO> helpTaskDTOs = transform(helpTasks);
        return helpTaskDTOs;
    }

    public List<HelpTaskDTO> transform(List<HelpTask> helpTasks) {
        List<HelpTaskDTO> helpTaskDTOs = new ArrayList<>();
        for (HelpTask helpTask : helpTasks) {
            HelpTaskDTO helpTaskDTO = new HelpTaskDTO();
            try {
                BeanUtils.copyProperties(helpTaskDTO, helpTask);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            Map param = new HashMap();

            for (Map.Entry<String, String> params : helpTask.getParams().entrySet()) {
                param.put(params.getKey(), params.getValue());
            }
            helpTaskDTO.setParame(param);
            helpTaskDTOs.add(helpTaskDTO);
        }
        return helpTaskDTOs;
    }
}
