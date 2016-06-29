package com.ucsmy.collection.services;

import com.ucsmy.collection.dto.CookieDTO;
import com.ucsmy.collection.dto.JobDTO;

import com.ucsmy.collection.dto.ProxyDTO;
import com.ucsmy.collection.dto.TasksDTO;
import com.ucsmy.collection.models.*;
import com.ucsmy.collection.repositories.JobRepository;
import org.apache.commons.beanutils.BeanUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by Administrator on 2015/11/18.
 */
@Service
@Transactional
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TasksService tasksService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SpiderJobService spiderJobService;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private ListOps listOps;

    private List<JobDTO> jobDTOs = new ArrayList<>();

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.job";
    public static final String CACHE_NAME_JOB = "cache.job.all";

    public JobDTO pop(String uuid, List<JobDTO> jobdtos) {
        Random random = new Random();
        if (jobdtos.size() == 0) {
            redisTemplate.delete("com.ucsmy.collection.services.JobServicefindAllByStatus1");
//            jobDTOs = findAllByStatus(1);
            return null;
        }
        int i = random.nextInt(jobdtos.size());
        JobDTO jobDTO = jobdtos.remove(i);
//        map.put("job", jobDTO);
        SpiderJob spiderJob = new SpiderJob();
        spiderJob.setJobId(jobDTO.getId());
        spiderJob.setUuid(uuid);
        spiderJob.setCreatedTime(new Date());
        spiderJob.setUpdatedTime(new Date());
        spiderJobService.save(spiderJob);
        if (jobdtos.size() == 0)
            redisTemplate.delete("com.ucsmy.collection.services.JobServicefindAllByStatus1");
        else
            redisTemplate.opsForValue().set("com.ucsmy.collection.services.JobServicefindAllByStatus1", jobdtos);
        return jobDTO;
    }

    public List<Job> findAll() {
        return jobRepository.findAll();
    }

    @Cacheable(value = CACHE_NAME_JOB, keyGenerator = "wiselyKeyGenerator",condition="#result != {}")
    public List<JobDTO> findAllByStatus(int status) {
        logger.info("I don't Execute sql,yes");
        List<Job> jobs = jobRepository.findAllByStatusOrderByIdAsc(status);
        List<JobDTO> jobDTOs = transform(jobs, 1);
        return jobDTOs;
    }

//    @Cacheable(value = CACHE_NAME_JOB,keyGenerator = "wiselyKeyGenerator")
    public List<Job> findJobByStatus(int status) {
        return jobRepository.findAllByStatusOrderByIdAsc(status);
    }

    public JobDTO saveJobDTO(Job job, int status) {
        JobDTO jobDTO = new JobDTO();
        try {
            BeanUtils.copyProperties(jobDTO, job);

            Map header = new HashMap();

            for (Map.Entry<String, String> headers : job.getHeaders().entrySet()) {
                header.put(headers.getKey(), headers.getValue());
            }

            Map login = new HashMap();
            for (Map.Entry<String, String> logins : job.getLogins().entrySet()) {
                login.put(logins.getKey(), logins.getValue());
            }
//            List<ProxyDTO> proxyDTOs = proxyService.transform(job.getProxys());
//            List<CookieDTO> cookieDTOs = cookieService.transform(job.getCookies());
            jobDTO.setHeader(header);
            jobDTO.setLogin(login);
            jobDTO.setCookieDTOs(cookieService.findByJobAndStatus(job,0));
//            jobDTO.setProxyDTOs(proxyDTOs);
            TasksDTO tasksDTO = tasksService.transformObj(job.getTasks());
            jobDTO.setTasksDTO(tasksDTO);
            //job.setStatus(1);
            //jobRepository.save(job);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return jobDTO;
    }

    public JobDTO pop(String uuid) {
        if (jobDTOs.size() == 0) {
            findAll();
        }
        Random random = new Random();
        int i = random.nextInt(jobDTOs.size());
        JobDTO jobDTO = jobDTOs.get(i);
        deleteJob(jobDTO);
        return jobDTO;
    }

    @CachePut(value = CACHE_NAME, key = "#jobDTO.id")
    public void deleteJob(JobDTO jobDTO) {
        jobDTOs.remove(jobDTO);
    }

    public List<JobDTO> transform(List<Job> jobs, int status) {
        List<JobDTO> jobDTOs = new ArrayList<>();
        for (Job job : jobs) {
            JobDTO jobDTO = saveJobDTO(job, status);
            jobDTOs.add(jobDTO);
        }
        return jobDTOs;
    }

    @Cacheable(value = CACHE_NAME_JOB, keyGenerator = "wiselyKeyGenerator")
    public Job get(Long jobId) {
        return jobRepository.findOne(jobId);
    }

    public List<Job> findAllByJobType(int jobType) {
        List<Job> jobs = jobRepository.findAllByJobTypeOrderByIdAsc(jobType);
        return jobs;
    }

    @Caching(
            evict = {
                    @CacheEvict(value = CACHE_NAME_JOB, allEntries = true),
                    @CacheEvict(value = CookieService.CACHE_NAME_COOKIE, allEntries = true)
            }
    )
    public void saveJob(Job job) {
        jobRepository.save(job);
    }

}
