package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.DataMsg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/12/30.
 */
@Repository
@Transactional
public interface DataMsgRepository extends JpaRepository<DataMsg,Long>{


    public List<DataMsg> findByNameAndStatusOrderByIdDesc(String key,Boolean status);
}
