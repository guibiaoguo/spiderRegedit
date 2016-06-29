package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface TokenRepository extends JpaRepository<Token,Long>{
}
