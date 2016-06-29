package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.HelpTaskDTO;
import com.ucsmy.collection.dto.TargetTaskDTO;
import com.ucsmy.collection.dto.TasksDTO;
import com.ucsmy.collection.models.Captcha;
import com.ucsmy.collection.models.TargetTask;
import com.ucsmy.collection.models.Tasks;
import com.ucsmy.collection.repositories.TasksRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */

@Service
public class TasksService {

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private HelpTaskService helpTaskService;

    @Autowired
    private TargetTaskService targetTaskService;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.tasks";
    public static final String CACHE_NAME_TASKS = "cache.tasks.all";

    @Cacheable(value = CACHE_NAME_TASKS,keyGenerator = "wiselyKeyGenerator")
    public List<TasksDTO> findAll(){
        List<Tasks> taskses = tasksRepository.findAll();
        List<TasksDTO> tasksDTOs = transform(taskses);
        return tasksDTOs;
    }

    public List<TasksDTO> transform(List<Tasks> taskses) {
        List<TasksDTO> tasksDTOs = new ArrayList<>();
        for (Tasks tasks : taskses) {
            TasksDTO tasksDTO = transformObj(tasks);
            tasksDTOs.add(tasksDTO);
        }
        return tasksDTOs;
    }

    public TasksDTO transformObj(Tasks tasks) {
        TasksDTO tasksDTO = new TasksDTO();
        List<HelpTaskDTO> helpTaskDTOs = helpTaskService.transform(tasks.getHelpTasks());
        List<TargetTaskDTO> targetTaskDTOs = targetTaskService.getTaragetTasks(tasks.getId());
        try {
            BeanUtils.copyProperties(tasksDTO,tasks);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        tasksDTO.setHelpTaskDTOs(helpTaskDTOs);
        tasksDTO.setTargetTaskDTOs(targetTaskDTOs);
        return tasksDTO;
    }

    public List<Tasks> findAllTask() {
        return tasksRepository.findAll();
    }
}
