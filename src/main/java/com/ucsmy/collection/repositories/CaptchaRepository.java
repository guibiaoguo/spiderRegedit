package com.ucsmy.collection.repositories;

import com.ucsmy.collection.models.Captcha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Administrator on 2015/11/18.
 */
@Repository
@Transactional
public interface CaptchaRepository extends JpaRepository<Captcha,Long>{
}
