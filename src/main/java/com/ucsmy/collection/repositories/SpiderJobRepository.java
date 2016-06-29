package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.SpiderJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/12/7.
 */
@Repository
@Transactional
public interface SpiderJobRepository extends JpaRepository<SpiderJob,Long>{
}
