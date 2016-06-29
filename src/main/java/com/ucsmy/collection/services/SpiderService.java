package com.ucsmy.collection.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ucsmy.collection.SpiderConfig;
import com.ucsmy.collection.dto.CollectionData;
import com.ucsmy.collection.dto.Link;
import com.ucsmy.collection.error.NotFoundException;
import com.ucsmy.collection.models.Job;
import com.ucsmy.collection.models.Spider;

import com.ucsmy.collection.models.SpiderLog;
import com.ucsmy.collection.models.TaskServer;
import com.ucsmy.collection.repositories.SpiderRepository;
import com.ucsmy.collection.util.*;
import com.ucsmy.collection.util.Base64;
import org.apache.commons.codec.binary.*;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.util.*;
import java.util.concurrent.BlockingDeque;


/**
 * @author Raysmond<i@raysmond.com>.
 */
@Service
public class SpiderService {
    private SpiderRepository spiderRepository;

    @Autowired
    private TaskServerService taskServerService;

    @Autowired
    private SpiderLogService spiderLogService;

    @Autowired
    private JobService jobService;

    @Autowired
    private SpiderConfig spiderConfig;

    @Autowired
    private SpiderApplicationService spiderApplicationService;

    private static final Logger logger = LoggerFactory.getLogger(SpiderService.class);

    public static final String CACHE_NAME = "cache.spider";
    public static final String CACHE_NAME_SPIDER = "cache.spider.all";

    @Autowired
    public SpiderService(SpiderRepository spiderRepository) {
        this.spiderRepository = spiderRepository;
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Cacheable(value = CACHE_NAME_SPIDER, key = "#root.method.name")
    public List<Spider> getAllSpider() {
        return spiderRepository.findAll();
    }

    @Cacheable(value = CACHE_NAME, key = "#spiderId.toString()")
    public Spider getSpider(Long spiderId) {
        logger.debug("Get Spider " + spiderId);

        Spider spider = spiderRepository.findOne(spiderId);

        if (spider == null) {
            throw new NotFoundException("Spider with id " + spiderId + " is not found.");
        }

        return spider;
    }

    @Caching(evict = {
            @CacheEvict(value = CACHE_NAME, key = "#spider.id"),
            @CacheEvict(value = CACHE_NAME, key = "#spider.spiderName"),
            @CacheEvict(value = CACHE_NAME, key = "#spider.clientIp"),
            @CacheEvict(value = CACHE_NAME, key = "#spider.nikeName"),
            @CacheEvict(value = CACHE_NAME, key = "#spider.uuid")
    })
    public Spider updateSpider(Spider spider) {
        return spiderRepository.save(spider);
    }

    public Map regedit(String uuid, Long jobId, String info, HttpServletRequest request,String version,String agentID,String mac,Integer count) {
        Map map = new HashedMap();
        try {
            Spider spider = null;
            SpiderLog spiderLog = new SpiderLog();
            TaskServer taskServer = taskServerService.findTopByOrderBySpiderCountDesc();
            if (StringUtils.isEmpty(uuid)) {
                spider = new Spider();
                spider.setStatus(1);
                uuid = UUID.randomUUID().toString().replace("-", "");
                spider.setUuid(uuid);
//                spider.setClientIp(getIpAddress(request));
                spider.setInfo(info);
                spider.setVersion(Double.parseDouble(version));
                spider.setAgentID(agentID);
                spider.setMac(agentID);
                spider = updateSpider(spider);
            } else {
                spider = spiderRepository.findByuuid(uuid);
//                BeanUtils.copyProperties(spiderLog,spider);
                spiderLog.setJobId(jobId);
                spiderLog.setInfo(URLDecoder.decode(info,"utf-8"));
                spiderLog.setAgentID(agentID);
                spiderLog.setMac(mac);
//                spiderLogService.save(spiderLog);
//                updateSpider(spider);
//                spider.setId(null);
                spiderLog.setUuid(uuid);
                spiderLog.setCount(count);
                spiderLog.setTaskServer(taskServer);
                spiderLogService.save(spiderLog);
            }

            taskServer.setSpiderCount(taskServer.getSpiderCount() + 1);
            taskServerService.updateTaskServer(taskServer);
            if (spider != null) {
                map.put("success", true);
                map.put("code", 200);
                map.put("uuid", uuid);
                map.put("msg", "保存成功");
                map.put("url", taskServer.getDomain());
            } else {
                map.put("code", 500);
                map.put("success", false);
                map.put("msg", "操作失败");
            }
        } catch (Exception e) {
            map.put("code", 500);
            map.put("success", false);
            map.put("msg", "操作失败");
            e.printStackTrace();
        }

        return map;
    }

    public Map saveCollectionData(String collectionData) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = new HashedMap();
        try {
            logger.info("**********************上传数据是" + collectionData.length()/1024.0 + "KB *********************");
            String password = spiderConfig.getData().getPassword();
//            String reValue = new Endecrypt().get3DESDecrypt(collectionData, password);
            String reValue = URLDecoder.decode(URLDecoder.decode(collectionData,"utf-8"),"utf-8");
            reValue = new String(Base64.decode(reValue),"utf-8");
            CollectionData data = objectMapper.readValue(reValue, CollectionData.class);
//            ListOperations<String, Object> listOperations = redisTemplate.opsForList();
            SetOperations<String,Map> setOperations = redisTemplate.opsForSet();
            Map<String,List<Map>> links = data.getLinks();
            List<Map> requests = data.getRequests();
            for (Map.Entry<String,List<Map>> link : links.entrySet()) {
//                for (int i = 0; i < link.getValue().size(); i++) {
//                    String priex = "";
//                    if (i > 0) {
//                        priex = i + "";
//                    }
//                    if(link.getValue().size() > 0)
//                        setOperations.add(link.getKey(),link.getValue());
//                }
//                setOperations.add(link.getKey(),link.getValue());
                setOperations.add(link.getKey(), link.getValue().toArray(new Map[link.getValue().size()]));
            }
            String key = data.getSourceLink();
//            HashOperations hash = redisTemplate.opsForHash();
            if(StringUtils.isNotEmpty(key)) {
    /*            try {
                    Map<String, Object> dataTemps = hash.entries(key);
                    for (Map.Entry<String, Object> dataTemp : dataTemps.entrySet()) {
                        String temp = dataTemp.getValue() != null ? dataTemp.getValue().toString() : "";
                        Set templinks = null;
                        if (StringUtils.isNotEmpty(temp)) {
                            templinks = objectMapper.readValue(temp, Set.class);
                        }
                        for (Map request : requests)
                            templinks.remove(request);
                        if (templinks.size() == 0) {
                            hash.delete(key, dataTemp.getKey());
                        } else {
                            dataTemps.put(dataTemp.getKey(),objectMapper.writeValueAsString(templinks));
                            hash.putAll(key, dataTemps);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                Calendar dalendar = Calendar.getInstance();
                int year = dalendar.get(Calendar.YEAR);
                String tmpKey = StringUtils.substringBefore(key,year + "");
                Set<Map> tmp1 = setOperations.members(key);
                if(tmp1.size() > 0)
                    setOperations.remove(key,tmp1.toArray(new Map[tmp1.size()]));
                for(int i = 0;i<requests.size();i++) {
                    Map request = requests.get(i);
//                    boolean flag = tmp1.remove(request);
                    int count = request!=null?(request.get("$count") != null ?Integer.parseInt(request.get("$count").toString()):0):6;
                    if(count == 0) {
                        tmp1.remove(request);
//                        setOperations.remove(key,request);
                    }
                    else if(count == 5) {
                        request.put("$count",4);
                        tmp1.remove(request);
//                        setOperations.remove(key,request);
                    } else if( count == 1) {
                        request.remove("$count");
                        tmp1.remove(request);
                        request.put("$count",1);
                        setOperations.add(tmpKey,request);
                    }
                    else if(count < 5){
//                        request.remove("$count");
                        request.put("$count",count - 1);
                        tmp1.remove(request);
                        request.put("$count",count);
                        setOperations.add(tmpKey,request);
                    }
                }
//                Set<Map> tmp = setOperations.difference(key,"0");
                if(tmp1.size() > 0) {
                    setOperations.add(key,tmp1.toArray(new Map[tmp1.size()]));
//                    setOperations.remove(key,tmp.toArray(new Map[tmp.size()]));
//                    setOperations.add(key,tmp1.toArray(new Map[tmp1.size()]));
                }
            }

            String destDir = spiderConfig.getData().getPath();
            File destF = new File(destDir);
            if (!destF.exists()) {
                destF.mkdirs();
            }

            int count = 0;
            StringBuffer collection = new StringBuffer();
            Map<String, List<Map>> datas = data.getDatas();
            for (Map.Entry<String,List<Map>> tmp:datas.entrySet()) {
                count += tmp.getValue().size();
                collection.append(tmp.getKey()).append(": ").append(tmp.getValue().size()).append(";");
            }
//            if(data.getDatas() != null && data.getDatas().size() > 0) {
//                File dataFile = new File(destDir + "/" + DateUtils.currtimeToString12());
//                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile, true));
//
//                for (Map.Entry<String, Map<String, Map>> dataTmp : datas.entrySet()) {
////                    List<Map> dataList = mapData.get(dataTmp.getKey()) != null ? mapData.get(dataTmp.getKey()):new ArrayList<>();
//                    List<Map> dataList = new ArrayList<>();
//                    for (int i = 0; i < dataTmp.getValue().size(); i++) {
//                        String priex = "";
//                        if (i > 0) {
//                            priex = i + "";
//                        }
//                        dataList.add(dataTmp.getValue().get("data" + priex));
//                    }
//                    if(dataList != null && dataList.size() > 0) {
//                        Map<String,List<Map>> mapData = new HashedMap();
//                        mapData.put(dataTmp.getKey(),dataList);
//                        bufferedWriter.write(objectMapper.writeValueAsString(mapData) + "\n");
//                    }
//                }
//                bufferedWriter.flush();
//                bufferedWriter.close();
//                map.put("count",data.getDatas().size());
//            }
            if(data.getDatas() != null && data.getDatas().size() > 0) {
                File dataFile = new File(destDir + "/" + DateUtils.currtimeToString12());
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(dataFile, true));
                bufferedWriter.write(objectMapper.writeValueAsString(data.getDatas()) + "\n");
                bufferedWriter.flush();
                bufferedWriter.close();
                map.put("count",count);
                map.put("collection",collection);
            }
            map.put("success", true);
            map.put("msg", "保存成功");
        } catch (Exception e) {
            map.put("success", false);
            map.put("count",0);
            map.put("msg", "操作失败");
            e.printStackTrace();
        } finally {

        }
        return map;
    }

    public Link getlinks(String linkname, Long length) {
//        ObjectMapper objectMapper = new ObjectMapper();
        Link link = new Link();
//        List<Map<String,Object>> links = new ArrayList<>();
        try {
            SetOperations<String,Map> setOperations = redisTemplate.opsForSet();
            long count = length <= setOperations.size(linkname)?length:setOperations.size(linkname);
            String key = linkname + DateUtils.getCurrDay();
            Set<Map> links = setOperations.distinctRandomMembers(linkname,length);
            if(links.size() > 0) {
                setOperations.remove(linkname,links.toArray(new Map[links.size()]));
                setOperations.add(key,links.toArray(new Map[links.size()]));
                link.setLinks(links);
                link.setSourceLink(key);
            }
//            for (long i = 0; i < count; i++) {
//                Map object = setOperations.pop(linkname);
//                links.add(object);
//                setOperations.add(key,object);
//            }
//            String key = "dataTemp" + DateUtils.dateToString(DateUtils.getAddHourTime(-2), "yyyyMMddHH");
//            Map dataTemp = hash.entries(key);
//            String temp = dataTemp.get(linkname) != null ? dataTemp.get(linkname).toString() : "";
//            Set<Object> templinks = null;
//            if (StringUtils.isNotEmpty(temp)) {
//                templinks = objectMapper.readValue(temp, Set.class);
//            } else {
//                templinks = new HashSet<>();
//            }
//            templinks.addAll(links);
//            if(templinks.size() > 0) {
//                dataTemp.put(linkname, objectMapper.writeValueAsString(templinks));
//                hash.putAll("dataTemp" + DateUtils.dateToString(DateUtils.getAddHourTime(-2), "yyyyMMddHH"), dataTemp);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            return link;
        }
//        for (int i = 0; i < links.size(); i++) {
//            listOperations.remove(linkname,i,links.get(i));
//        }
        return link;
    }


    public Map getCaptcha(String language, MultipartFile captcha) {
        Map map = new HashMap();
        try {
            byte[] bytes = captcha.getBytes();
            String destDir = spiderConfig.getCaptchaPath();
            File destF = new File(destDir);
            if (!destF.exists()) {
                destF.mkdirs();
            }
            File upload = new File(destDir + captcha.getOriginalFilename());
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(upload));
            stream.write(bytes);
            stream.close();
            String code = CaptchaUtil.tesseract(upload, language);
            map.put("code", code);
            map.put("success", "true");
        } catch (Exception e) {
            map.put("success", "false");
            e.printStackTrace();
        }
        return map;
    }

    public Map getVersion(double version) {
        return spiderApplicationService.findByVersionAfter(version);
    }

    public void downlod(String fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            String path = spiderConfig.getDownPath();
            InputStream inputStream = new FileInputStream(new File(path
                    + File.separator + fileName));

            OutputStream os = response.getOutputStream();
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            // 这里主要关闭。
            os.close();
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map getFinancial(String reqTime, String companyCode) {

        Map map = new HashedMap();
        List<Map> data = new ArrayList<>();
        Map bankTemp = new HashedMap();

        if(companyCode == "msyidai") {
            bankTemp.put("publishDate","");
            bankTemp.put("bank","");
            bankTemp.put("yieldRate","");
            bankTemp.put("amount","");
            bankTemp.put("actualFinAmount","");
            bankTemp.put("financingMaturity","");
        } else if(companyCode == "gdnybank") {
            bankTemp.put("publishDate","");
            bankTemp.put("bank","");
            bankTemp.put("yieldRate","");
            bankTemp.put("amount","");
            bankTemp.put("actualFinAmount","");
            bankTemp.put("financingMaturity","");
        } else if ( companyCode == "lzbank" ) {
            bankTemp.put("publishDate","");
            bankTemp.put("bank","");
            bankTemp.put("yieldRate","");
            bankTemp.put("amount","");
            bankTemp.put("actualFinAmount","");
            bankTemp.put("financingMaturity","");
        } else if( companyCode == "nbcb") {
            bankTemp.put("publishDate","");
            bankTemp.put("bank","");
            bankTemp.put("yieldRate","");
            bankTemp.put("amount","");
            bankTemp.put("actualFinAmount","");
            bankTemp.put("financingMaturity","");
        } else if( companyCode == "yinhang") {
            bankTemp.put("publishDate","");
            bankTemp.put("bank","");
            bankTemp.put("yieldRate","");
            bankTemp.put("amount","");
            bankTemp.put("actualFinAmount","");
            bankTemp.put("financingMaturity","");
        }
        data.add(bankTemp);
        map.put("data",data);
        map.put("code",200);
        map.put("msg","成功");
        return map;
    }
}
