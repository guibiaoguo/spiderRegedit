package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.TargetTaskDTO;
import com.ucsmy.collection.models.TargetTask;
import com.ucsmy.collection.repositories.TargetTaskRepository;
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
public class TargetTaskService {

    @Autowired
    private TargetTaskRepository targetTaskRepository;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.targetTask";
    public static final String CACHE_NAME_TARGET_TASK = "cache.targetTask.all";

    @Cacheable(value = CACHE_NAME_TARGET_TASK, key = "#root.targetClass + #root.methodName")
    public List<TargetTaskDTO> findAll(){
        List<TargetTask> targetTasks = targetTaskRepository.findAll();
        List<TargetTaskDTO> targetTaskDTOs = transform(targetTasks);
        return targetTaskDTOs;
    }

    public List<TargetTaskDTO> transform(List<TargetTask> targetTasks) {
        List<TargetTaskDTO> targetTaskDTOs = new ArrayList<>();
        for(TargetTask targetTask :targetTasks) {
            TargetTaskDTO targetTaskDTO = new TargetTaskDTO();
            try {
                BeanUtils.copyProperties(targetTaskDTO, targetTask);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            targetTaskDTOs.add(targetTaskDTO);
        }
        return targetTaskDTOs;
    }

    public List<TargetTaskDTO> getTaragetTasks(long id) {
        List<TargetTask> targetTasks = targetTaskRepository.findByTaskId(id);
        List<TargetTaskDTO> targetTaskDTOs = transform(targetTasks);
        return targetTaskDTOs;
    }
}
