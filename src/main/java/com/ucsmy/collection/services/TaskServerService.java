package com.ucsmy.collection.services;

import com.ucsmy.collection.models.TaskServer;
import com.ucsmy.collection.models.Tasks;
import com.ucsmy.collection.repositories.TaskServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2015/12/4.
 */
@Service
public class TaskServerService {

    @Autowired
    private TaskServerRepository taskServerRepository;


    public TaskServer findTopByOrderBySpiderCountDesc() {
        return taskServerRepository.findTopByOrderBySpiderCountDesc();
    }

    public void updateTaskServer(TaskServer taskServer) {
        taskServerRepository.save(taskServer);
    }

}
