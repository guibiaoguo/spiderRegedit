package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface JobRepository extends JpaRepository<Job,Long>{

    List<Job> findAllByStatusOrderByIdAsc(int status);
    List<Job> findAllByJobTypeOrderByIdAsc(int jobType);
}
