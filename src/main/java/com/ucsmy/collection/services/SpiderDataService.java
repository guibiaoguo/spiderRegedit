package com.ucsmy.collection.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucsmy.collection.SpiderConfig;
import com.ucsmy.collection.dao.SpiderDataDao;
import com.ucsmy.collection.models.*;
import com.ucsmy.collection.util.AuthenticationUtil;
import com.ucsmy.collection.util.DateUtils;
import com.ucsmy.collection.util.LoggerUtils;
import com.ucsmy.collection.util.ProxyPool;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.proxy.ProxyUtil;

import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Administrator on 2015/12/31.
 */

@Service
public class SpiderDataService {

    private Logger logger = LoggerUtils.getLogger(getClass());

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SpiderDataDao spiderDataDao;

    @Autowired
    private SpiderConfig spiderConfig;

    @Autowired
    private DataMsgService dataMsgService;

    @Autowired
    private JobService jobService;

    @Autowired
    private TasksService tasksService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisConfigService redisConfigService;

    @Autowired
    private ProxyService proxyService;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    public void insertData() {
        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        try {
            File path = new File(spiderConfig.getData().getPath());
            if (!path.exists()) {
                path.mkdir();
            }
            Set<String> tables = new HashSet<>();
            for (File file : path.listFiles()) {
                DataMsg dataMsg = dataMsgService.findByNameAndStatusOrderByIdDesc(file.getName(), true);
                if (dataMsg != null && dataMsg.isStatus()) {
                    continue;
                } else {
                    dataMsg = new DataMsg();
                    dataMsg.setName(file.getName());
//                    dataMsgService.save(dataMsg);
                }
//            File file = new File(path + "/" + DateUtils.dateToString(DateUtils.getAddHourTime(-1), "yyyyMMddHH"));
//                dataMsg.setCount(count + 1);
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//                reader.mark(count + 1);
                String temp = null;
                logger.info("--------------------------------------------------------");
                int i = 0;
                StringBuffer info = new StringBuffer(file.getName()).append(":");
                long startTime = System.currentTimeMillis();
                while ((temp = reader.readLine()) != null) {
                    i++;
                    try {
                        logger.info("##################################################");
                        Map<String, List<Map>> collectionData = objectMapper.readValue(temp, Map.class);
                        logger.info("*****************************************************");
                        for (Map.Entry<String, List<Map>> datas : collectionData.entrySet()) {
                            List<Map> insertDatas = datas.getValue();
                            logger.info("表" + datas.getKey() + "总数是" + insertDatas.size());
                            Map tableMap = new HashedMap();
                            tableMap.put(datas.getKey(), spiderDataDao.getAllTableName(datas.getKey()));
                            Boolean flag = tableMap.get(datas.getKey()) != null ? (Boolean) tableMap.get(datas.getKey()) : false;
                            if (!flag) {
                                spiderDataDao.createTable(datas.getKey(), insertDatas.get(0));
                                tableMap.put(datas.getKey(), true);
                            }
                            String time = DateUtils.currtimeToString14();
                            spiderDataDao.insertData(datas, time);
                            tables.add(datas.getKey());
//                            spiderDataDao.distinct(datas.getKey());

//                            for (Map insertData : insertDatas) {
//                                try {
////                                    insertData.put("product","");
////                                    insertData.put("address","");
////                                    Boolean flag = tableMap.get(datas.getKey()) != null ? (Boolean) tableMap.get(datas.getKey()) : false;
////                                    if (flag) {
////                                        if (spiderDataDao.selectObject(datas.getKey(), insertData) == 0) {
//                                            spiderDataDao.insertData(datas.getKey(), insertData);
////                                        }
////                                    } else {
////                                        spiderDataDao.createTable(datas.getKey(), insertData);
////                                        spiderDataDao.insertData(datas.getKey(),insertData);
////                                        tableMap.put(datas.getKey(),true);
////                                    }
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                    info.append(datas.getKey()).append(objectMapper.writeValueAsString(insertData)).append("\n");
//                                }
//                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    logger.info("*****************************************************");
                }

                long endTime = System.currentTimeMillis();
                logger.info("总共花了 " + (endTime - startTime));
                reader.close();
                dataMsg.setInfo(info.toString());
                dataMsg.setStatus(true);
                dataMsgService.save(dataMsg);
                File backpath = new File(spiderConfig.getData().getBack());
                if (!backpath.exists()) {
                    backpath.mkdir();
                }
                backpath = new File(spiderConfig.getData().getBack() + "/" + DateUtils.currtimeToString8());
                if (!backpath.exists()) {
                    backpath.mkdir();
                }
                File backFile = new File(spiderConfig.getData().getBack() + "/" + DateUtils.currtimeToString8() + "/" + file.getName());
                file.renameTo(backFile);
//                file.delete();

                logger.info("--------------------------------------------------------");
            }
            logger.info("去重开始 *****************************************************");
            for (String table : tables) {
//                spiderDataDao.distinct(table);
            }
            logger.info("去重结束 *****************************************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveLink() {
//        HashOperations hash = redisTemplate.opsForHash();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations<String, Map> setOperations = redisTemplate.opsForSet();
        List<Tasks> taskses = tasksService.findAllTask();
        for (Tasks tasks : taskses) {
            String tmpkey1 = tasks.getSourceTable() + DateUtils.getCurrDay();
            String tmpkey2 = tasks.getSourceTable() + DateUtils.dateIncreaseByDay(DateUtils.getCurrDay(), -1);
            Map<String, Object> tmp = setOperations.pop(tmpkey1);
            while (tmp != null) {
                tmp.remove("$count");
                setOperations.add(tasks.getSourceTable(), tmp);
                tmp = setOperations.pop(tmpkey1);
            }
            tmp = setOperations.pop(tmpkey2);
            while (tmp != null) {
                tmp.remove("$count");
                setOperations.add(tasks.getSourceTable(), tmp);
                tmp = setOperations.pop(tmpkey2);
            }
//            Set<Map> tmpSet = setOperations.difference(tasks.getSourceTable()+DateUtils.getCurrDay(),"0");
//            Set<Map> tmpSet1 = setOperations.difference(tasks.getSourceTable()+DateUtils.dateIncreaseByDay(DateUtils.getCurrDay(),-1),"0");
//            if(tmpSet.size() > 0) {
//                setOperations.add(tasks.getSourceTable(),tmpSet.toArray(new Map[tmpSet.size()]));
//                setOperations.remove(tasks.getSourceTable() + DateUtils.getCurrDay(),tmpSet.toArray(new Map[tmpSet.size()]));
//            }
//            if(tmpSet1.size() > 0) {
//                setOperations.add(tasks.getSourceTable(),tmpSet1.toArray(new Map[tmpSet1.size()]));
//                setOperations.remove(tasks.getSourceTable() + DateUtils.dateIncreaseByDay(DateUtils.getCurrDay(),-1),tmpSet1.toArray(new Map[tmpSet1.size()]));
//            }
        }
//        Calendar dalendar = Calendar.getInstance();
//        int hour = dalendar.get(Calendar.HOUR_OF_DAY);
//        hour = -hour;
//        for (int i = hour; i <= 0; i++) {
//            try {
//                String key = "dataTemp" + DateUtils.dateToString(DateUtils.getAddHourTime(i), "yyyyMMddHH");
//                logger.info("临时队列" + key);
////            String key = "dataTemp2016011008";
//                Map<String, Object> dataTemps = hash.entries(key);
//                for (Map.Entry<String, Object> dataTemp : dataTemps.entrySet()) {
//                    String temp = dataTemp.getValue() != null ? dataTemp.getValue().toString() : "";
//                    List<Object> templinks = null;
//                    if (StringUtils.isNotEmpty(temp)) {
//                        templinks = objectMapper.readValue(temp, List.class);
//                    }
//                    hash.delete(key, dataTemp.getKey());
//                    if (templinks != null && templinks.size() > 0)
////                        listOperations.leftPushAll(dataTemp.getKey(), templinks.toArray(new Map[templinks.size()]));
//                        setOperations.add(dataTemp.getKey(), templinks.toArray(new Map[templinks.size()]));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void startJob() {
        List<Job> jobs = jobService.findJobByStatus(0);
//        ListOperations listOperations = redisTemplate.opsForList();
//        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        for (Job job : jobs) {
            job.setStatus(1);
            jobService.saveJob(job);
            createRedis(job, false);
        }
    }

    public String getNum(long n, int count) {
        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMinimumIntegerDigits(count);
        formatter.setGroupingUsed(false);
        String s = formatter.format(n);
//        logger.info(s);
        return s;
    }

    public void stopJob() {
        List<Job> jobs = jobService.findJobByStatus(1);
//        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        for (Job job : jobs) {
            Tasks tasks = job.getTasks();
//            long len = listOperations.size(tasks.getSourceTable());
            Long len = setOperations.size(tasks.getSourceTable());
            if (len == 0) {
                job.setStatus(2);
                jobService.saveJob(job);
            }
        }
    }

    public void test() {
        SetOperations<String, Map> setOperations = redisTemplate.opsForSet();
        Map map = setOperations.pop("qichachaDetail");
        while (map != null) {
            Map tmp = new HashedMap();
            tmp.put("unique", map.get("unique"));
            tmp.put("companyname", map.get("companyname").toString().replaceAll("<[^>]*>", ""));
            setOperations.add("qichachaDetail", tmp);
            map = setOperations.pop("qichachaDetail2");
        }
    }

    public void createRedis(Job job, boolean flag) {
        RedisConfig redisConfig = job.getRedisConfig();
        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        List<Map> tempMap = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        String link = redisConfig != null ? redisConfig.getLink() : "";
        logger.info("开始生成队列---------------" + link);
        long start = 0;
        long end = 0;
        if (redisConfig != null) {
            start = redisConfig.getStart();
            end = redisConfig.getEnd();
        }

        if (flag && end > 10) {
            end = (long) Math.ceil(end / 10.0) > 10 ? (long) Math.ceil(end / 10.0) : 10;
            redisConfig.setEnd(end);
            redisConfigService.save(redisConfig);
        }
        if (redisConfig != null && redisConfig.getType() == 0) {
            String temp = null;
            for (long i = start; i < (start + end); i++) {
                try {
                    Map map = new HashMap();
                    temp = StringUtils.replace(redisConfig.getUrl(), "$", getNum(i, redisConfig.getCount()));
                    map.put("url", temp);
                    map.put("id", i + "");
                    if (StringUtils.isNotEmpty(redisConfig.getCname()))
                        map.put(redisConfig.getCname(), i + "");
                    tempMap.add(map);
                    if (i > 10000) {
                        setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
                        tempMap.clear();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//                    listOperations.leftPush(link, map);
            }
            if (tempMap.size() > 0)
                setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
//            logger.info(temp);
        } else if (redisConfig != null && redisConfig.getType() == 1) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(redisConfig.getFilename()))));
                String temp = null;
                long i = 1;
                while ((temp = reader.readLine()) != null) {
                    try {
                        if (i < start) {
                            i++;
                            continue;
                        } else if (i > (start + end)) {
                            break;
                        }
                        Map map = new HashMap();
                        String temp1 = StringUtils.replace(redisConfig.getUrl(), "$", URLEncoder.encode(temp, "utf-8"));
                        map.put("url", temp1);
                        map.put("id", i + "");
                        map.put(redisConfig.getCname(), temp);
                        tempMap.add(map);
//                        listOperations.leftPush(link, map);
                        if (i > 1000) {
                            setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
                            tempMap.clear();
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (tempMap.size() > 0)
                    setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
//                logger.info(temp);
                reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (redisConfig != null && redisConfig.getType() == 2) {
            try {

                List<Map<String, Object>> results = jdbcTemplate.queryForList("SELECT * from " + redisConfig.getFilename() + " LIMIT  " + start + "," + end);
                String url = null;
                long i = 0;
                for (Map<String, Object> result : results) {
                    try {
                        String cname = result.get(redisConfig.getCname()).toString();
                        url = StringUtils.replace(redisConfig.getUrl(), "$", URLEncoder.encode(cname, "utf-8"));
                        result.put("url", url);
//                        listOperations.leftPush(link, result);
                        tempMap.add(result);
                        if (i > 1000) {
                            setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
                            tempMap.clear();
                        }
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (tempMap.size() > 0)
                    setOperations.add(link, tempMap.toArray(new Map[tempMap.size()]));
//                logger.info(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.info("生成队列" + link + "已经完成");
    }

    public void restartJob(int jobType) {
        List<Job> jobs = jobService.findAllByJobType(jobType);
//        ListOperations listOperations = redisTemplate.opsForList();
        SetOperations setOperations = redisTemplate.opsForSet();
        for (Job job : jobs) {
            try {
                Tasks tasks = job.getTasks();
                String linkSource = tasks.getSourceTable();
                logger.info("队列是" + linkSource);
                Long len = setOperations.size(linkSource);
                if (len == 0 || job.getStatus() == 2) {
                    job.setStatus(1);
                    jobService.saveJob(job);
                    createRedis(job, true);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 更新cookie
     */
    public void reflash() {
        try {
            List<Cookie> cookies = cookieService.findByStatus(0);
            for (Cookie cookie : cookies) {
                StringBuffer img = new StringBuffer();
                AuthenticationUtil authenticationUtil = new AuthenticationUtil();
                AuthenticationUtil.getHeaders().add(new BasicHeader("Cookie", cookie.getCookie()));
                String content = authenticationUtil.getText("http://www.qichacha.com/company_getinfos?unique=9cce0780ab7644008b73bc2120479d31&companyname=%E5%B0%8F%E7%B1%B3%E7%A7%91%E6%8A%80%E6%9C%89%E9%99%90%E8%B4%A3%E4%BB%BB%E5%85%AC%E5%8F%B8&tab=base");
                authenticationUtil.getCheckCode(content, "<li><label.*统一社会信用代码：  </label>(.*?)</li>|<li><label.*注册号：  </label>(.*?)</li>;regex", img);
                logger.info(img.toString());
                if (StringUtils.isEmpty(img.toString())) {
                    cookie.setStatus(1);
                    cookieService.save(cookie);
                }
                authenticationUtil.close();
                java.lang.Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getProxyList(List<Proxy> proxys) {
        List<String[]> proxyList = new ArrayList<String[]>();
        for (Proxy proxy : proxys) {
            proxyList.add(new String[]{proxy.getProxyHost(), proxy.getProxyPort()});
        }
        return proxyList;
    }

    public void reflashPxoy() {
        List<Proxy> proxys = proxyService.findFirst100ByStatus(0);
        for(Proxy proxy:proxys) {
            try {
                HttpHost item = new HttpHost(InetAddress.getByName(proxy.getProxyHost()), Integer.valueOf(proxy.getProxyPort()));
                if(ProxyUtil.validateProxy(item)) {
                    proxy.setStatus(1);
                    proxyService.save(proxy);
                } else {
                    proxyService.delete(proxy);
                }
            } catch ( Exception e) {
                e.printStackTrace();
            }

        }
    }
}

class RedisThread extends Thread {

    @Override
    public void run() {
        super.run();
    }
}