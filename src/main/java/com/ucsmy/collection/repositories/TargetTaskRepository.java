package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.TargetTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface TargetTaskRepository extends JpaRepository<TargetTask,Long>{

    @Query("SELECT t FROM TargetTask t WHERE t.tasks.id = ?1 order by t.id asc,t.saveTable ASC")
    List<TargetTask> findByTaskId(long id);
}
