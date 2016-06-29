package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.SpiderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/12/29.
 */
@Transactional
@Repository
public interface SpiderLogRepository extends JpaRepository<SpiderLog,Long>{
}
