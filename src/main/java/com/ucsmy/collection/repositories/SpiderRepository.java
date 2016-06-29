package com.ucsmy.collection.repositories;
import com.ucsmy.collection.models.Spider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Bill on 2015/11/15.
 */
@Repository
@Transactional
public interface SpiderRepository extends JpaRepository<Spider, Long> {

    Spider findByuuid(String spiderId);
}
