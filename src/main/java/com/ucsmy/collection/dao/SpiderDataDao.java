package com.ucsmy.collection.dao;

import com.google.common.hash.HashCode;
import com.ucsmy.collection.util.CityHash;
import com.ucsmy.collection.util.DateUtils;
import com.ucsmy.collection.util.LoggerUtils;
import org.apache.catalina.util.MD5Encoder;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Administrator on 2015/12/30.
 */
@Component
public class SpiderDataDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private Logger logger = LoggerUtils.getLogger(getClass());

    /**
     * 创建表，添加记录
     *
     * @param tableName
     * @param obj
     * @return
     */
    public int insertObject(String tableName, Map obj) throws Exception {
        int re = 0;
        // 判断数据库是否已经存在这个名称的表，如果有某表  则保存数据；否则动态创建表之后再保存数据
        if (getAllTableName(tableName)) {
//            if (selectObject(jdbcTemplate, tableName, obj) == 0) {
            re = insertData(tableName, obj);
//            }
        } else {
            re = createTable(tableName, obj);
            re = insertData(tableName, obj);
        }

        return re;
    }

    /**
     * 根据表名称创建一张表
     *
     * @param tableName
     */
    public int createTable(String tableName, Map map) throws Exception {
        int re;
        StringBuffer sb = new StringBuffer("");
        StringBuffer tmp = new StringBuffer("");
        StringBuffer official = new StringBuffer("");
        String tmpTable = tableName + "_tmp";
        tmp.append("CREATE TABLE `" + tmpTable + "` (");
        official.append("CREATE TABLE `" + tableName + "` (");
        sb.append(" `auto_id` int(11) NOT NULL AUTO_INCREMENT,");
        Set<String> set = map.keySet();
        for (String key : set) {
            sb.append("`" + key + "` longtext DEFAULT NULL,");
        }

        sb.append("`storage_time` varchar(14) DEFAULT NULL,");
        sb.append("`hashcode` varchar(128) DEFAULT NULL,");
//        sb.append(" `tableName` varchar(255) DEFAULT '',");
        sb.append(" PRIMARY KEY (`auto_id`)");
//        sb.append(", PRIMARY KEY (`hashcode`)");
//        sb.append(" PRIMARY KEY ("+StringUtils.join(set.toArray(new String[set.size()]),",")+")");
        tmp.append(sb).append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        official.append(sb).append(",UNIQUE KEY `hashcode` (`hashcode`)").append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        re = jdbcTemplate.update(tmp.toString());
        re = jdbcTemplate.update(official.toString());
        return re;
    }

    /**
     * 拼接语句，往表里面插入数据
     */
    public int saveObj(JdbcTemplate jdbcTemplate, String tableName, Map map) throws Exception {
        int re = 0;
        try {
            List parames = new ArrayList();
            String sql = " insert into " + tableName + " (";
            Set<String> set = map.keySet();
            for (String key : set) {
                sql += (key + ",");
            }
            sql += " tableName ) ";
            sql += " values ( ";
            for (String key : set) {
                sql += ("?,");
                parames.add(map.get(key));
            }
            sql += ("? ) ");
            parames.add(tableName);
            re = jdbcTemplate.update(sql, parames.toArray(new String[parames.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    /**
     * 拼接语句，往表里面插入数据
     */
    public int insertData(String tableName, Map<String, Object> map) {
        int re = 0;
        try {
            List keys = new ArrayList();
            List parames = new ArrayList();
            List value = new ArrayList();
            StringBuffer buffer = new StringBuffer("");
            buffer.append(" INSERT INTO ").append(tableName).append(" ( ");

            for (Map.Entry<String, Object> data : map.entrySet()) {
                keys.add("`" + data.getKey() + "`");
                parames.add(data.getValue());
                value.add("?");
            }

            keys.add("`storage_time`");
            parames.add(DateUtils.currtimeToString14());
            value.add("?");
            buffer.append(StringUtils.join(keys, ",")).append("  ) values ( ").append(StringUtils.join(value, ",")).append(" ) ");
            re = jdbcTemplate.update(buffer.toString(), parames.toArray(new String[parames.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return re;
    }

    /**
     * 拼接语句，往表里面插入数据
     */
    public int insertData(Map.Entry<String, List<Map>> datas,String time) {
        int[] re;
        try {
            Map<String, Object> dataTemp = datas.getValue().get(0);
            List keys = new ArrayList();
            List parames = new ArrayList();
            List value = new ArrayList();
            StringBuffer buffer = new StringBuffer("");
//            buffer.append(" INSERT INTO ").append(datas.getKey()).append(" ( ");
            buffer.append(" insert INTO ").append(datas.getKey() +"_tmp").append(" ( ");

            for (Map.Entry<String, Object> data : dataTemp.entrySet()) {
                keys.add("`" + data.getKey() + "`");
//                parames.add(data.getValue());
                value.add("?");
            }

//            for (Map<String, Object> map : datas.getValue()) {
//                for (Map.Entry<String, Object> data : map.entrySet()) {
//                    parames.add(data.getValue());
//                }
//                parames.add(DateUtils.currtimeToString14());
//            }
            keys.add("`storage_time`");
            value.add("?");
            keys.add("`hashcode`");
            value.add("?");
            buffer.append(StringUtils.join(keys, ",")).append("  ) values ( ").append(StringUtils.join(value, ",")).append(" ) ");
//            logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
            re = jdbcTemplate.batchUpdate(buffer.toString(), new MyBatchPreparedStatementSetter(datas.getValue(),time));
//            logger.info("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&");
//            re = jdbcTemplate.update(buffer.toString(), parames.toArray(new String[parames.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void distinct(String tableName) throws Exception{
        String tmpTable = tableName + "_tmp";
//        jdbcTemplate.update("truncate  TABLE " + tableName);
        jdbcTemplate.update("DELETE  FROM " + tableName);
        jdbcTemplate.update("INSERT INTO " + tableName + " SELECT * FROM (SELECT * FROM "+tmpTable+" ORDER BY storage_time DESC ) AS a GROUP BY a.hashcode ");
//        jdbcTemplate.update("truncate TABLE " + tmpTable);
        jdbcTemplate.update("DELETE FROM " + tmpTable);
        jdbcTemplate.update("INSERT into "+tmpTable+" SELECT * from "+tableName+"");
    }

    private class MyBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
        final List<Map> temList;
        private String time;

        public MyBatchPreparedStatementSetter(List<Map> list,String time) {
            temList = list;
            this.time = time;
        }

        public int getBatchSize() {
            return temList.size();
        }

        public void setValues(PreparedStatement ps, int i)
                throws SQLException {
//            for(Map<String,Object> map :temList) {
            int j = 1;
            Map<String, Object> datas = temList.get(i);
            StringBuffer buffer = new StringBuffer(8000);
            for (Map.Entry<String, Object> data : datas.entrySet()) {
                String x = data.getValue() != null ? data.getValue().toString() : "";
                ps.setString(j++, x);
                if(StringUtils.indexOfAny(data.getKey(),new String[]{"capture_time","source_url","source_name","source_type"}) == -1)
                    buffer.append(x);
            }
            ps.setString(j++, time);
            ps.setString(j++, CityHash.cityHash64(buffer.toString().getBytes(), 0, buffer.toString().getBytes().length) + "");
        }
    }

    /**
     * 拼接语句，往表里面插入数据
     */
    public int updateData(JdbcTemplate jdbcTemplate, String tableName, Map<String, Object> map) throws Exception {
        int re = 0;
        List keys = new ArrayList();
        List parames = new ArrayList();
        List value = new ArrayList();
        StringBuffer buffer = new StringBuffer("");
        try {
            buffer.append(" update ").append(tableName).append(" set ");
            for (Map.Entry<String, Object> data : map.entrySet()) {
                keys.add("`" + data.getKey() + "`");
                parames.add(data.getValue());
                value.add("?");
            }
            buffer.append(StringUtils.join(keys, ",")).append("  ) values ( ").append(StringUtils.join(value, ",")).append(" ) ");
            re = jdbcTemplate.update(buffer.toString(), parames.toArray(new String[parames.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re;
    }

    /**
     * 拼接语句，往表里面查询有没有数据
     */
    public int selectObject(String tableName, Map<String, Object> map) {
        List<Map<String, Object>> re = new ArrayList<>();
        try {
            List parames = new ArrayList();
            StringBuffer buffer = new StringBuffer("");
            buffer.append("SELECT 1 FROM ").append(tableName).append(" where 1=1 ");
            for (Map.Entry<String, Object> data : map.entrySet()) {
                String msg = data.getValue() != null ? data.getValue().toString() : "";
                if (StringUtils.isEmpty(msg)) {
                    buffer.append(" and `").append(data.getKey()).append("` is NULL ");
                } else {
                    buffer.append(" and `").append(data.getKey()).append("` = ? ");
                    parames.add(data.getValue());
                }
            }
            re = jdbcTemplate.queryForList(buffer.toString(), parames.toArray(new String[parames.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return re.size();
    }

    /**
     * 查询数据库是否有某表
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    public boolean getAllTableName(String tableName) throws Exception {
        Connection conn = jdbcTemplate.getDataSource().getConnection();
        ResultSet tabs = null;
        try {
            DatabaseMetaData dbMetaData = conn.getMetaData();
            String[] types = {"TABLE"};
            tabs = dbMetaData.getTables(null, null, tableName, types);
            if (tabs.next()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            tabs.close();
            conn.close();
        }
        return false;
    }

}
