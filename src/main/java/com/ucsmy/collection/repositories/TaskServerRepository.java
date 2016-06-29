package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.TaskServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/12/4.
 */
@Transactional
@Repository
public interface TaskServerRepository extends JpaRepository<TaskServer,Long>{

    TaskServer findTopByOrderBySpiderCountDesc();
}
