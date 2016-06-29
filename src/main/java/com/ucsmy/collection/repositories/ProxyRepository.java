package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.Proxy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface ProxyRepository extends JpaRepository<Proxy,Long>{
    List<Proxy> findByStatus(int i);
    List<Proxy> findFirst100ByStatus(int i);
}
