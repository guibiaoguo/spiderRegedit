package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.HelpTask;
import com.ucsmy.collection.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface HelpTaskRepository extends JpaRepository<HelpTask,Long>{
    List<HelpTask> findByTasks(Tasks tasks);
}
