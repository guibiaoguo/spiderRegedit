package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.SpiderApplicaton;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/1/8.
 */

@Repository
@Transactional
public interface SpiderApplicationRepository extends JpaRepository<SpiderApplicaton,Long> {

    List<SpiderApplicaton> findByVersionAfterOrderByVersionDesc(double version);
}
