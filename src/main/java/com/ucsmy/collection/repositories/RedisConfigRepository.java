package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.Cookie;
import com.ucsmy.collection.models.Job;
import com.ucsmy.collection.models.RedisConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface RedisConfigRepository extends JpaRepository<RedisConfig,Long>{

}
