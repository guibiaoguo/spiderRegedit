package com.ucsmy.collection.util;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.AuthSchemes;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Json;
import us.codecraft.webmagic.selector.PlainText;
import us.codecraft.webmagic.selector.Selectable;

import javax.imageio.ImageIO;
import javax.net.ssl.SSLException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/11/9.
 */
public class AuthenticationUtil {

    private static CloseableHttpClient httpClient;
    private static List<Header> headers;
    private static CloseableHttpResponse response;
    private static RequestConfig requestConfig;
    private static BlockingQueue<Map> queue = null;
    private static JdbcTemplate jdbcTemplate;

    public static void main(String[] args) throws Exception {
        AuthenticationUtil authenticationUtil = new AuthenticationUtil();
        String content = authenticationUtil.getText("http://qichacha.com/firm_GD_2d56b50e7c21481b5f970233b26378a7");
//        String sourceText = "{\"CurrentPage\":1,\"TotalItemCount\":156,\"PageSize\":10,\"Items\":[{\"Count\":1,\"Id\":\"933ed90e8f64b5bc\",\"IdCard\":\"1101051978****0454\",\"FactName\":\"刘斌\",\"BirthPlace\":\"北京市北京市朝阳区\",\"Sex\":\"男\"},{\"Count\":2,\"Id\":\"7be0f577c0bea973\",\"IdCard\":\"1101081982****6354\",\"FactName\":\"刘斌\",\"BirthPlace\":\"北京市北京市海淀区\",\"Sex\":\"男\"},{\"Count\":3,\"Id\":\"e394a428567fc877\",\"IdCard\":\"1201041973****0837\",\"FactName\":\"刘斌\",\"BirthPlace\":\"天津市天津市南开区\",\"Sex\":\"男\"},{\"Count\":4,\"Id\":\"4223e65f9a41398c\",\"IdCard\":\"1329301980****0000\",\"FactName\":\"刘斌\",\"BirthPlace\":\"河北省\",\"Sex\":\"男\"},{\"Count\":5,\"Id\":\"0c9f612e53147daa\",\"IdCard\":\"1401071989****4815\",\"FactName\":\"刘斌\",\"BirthPlace\":\"山西省太原市杏花岭区\",\"Sex\":\"男\"},{\"Count\":6,\"Id\":\"8a8dbdb79ce6036d\",\"IdCard\":\"1501221963****051X\",\"FactName\":\"刘斌\",\"BirthPlace\":\"内蒙古自治区呼和浩特市托克托县\",\"Sex\":\"男\"},{\"Count\":7,\"Id\":\"412558821cbed4e6\",\"IdCard\":\"1501221980****1015\",\"FactName\":\"刘斌\",\"BirthPlace\":\"内蒙古\",\"Sex\":\"男\"},{\"Count\":8,\"Id\":\"b7b194c4253baed4\",\"IdCard\":\"1501241988****0159\",\"FactName\":\"刘斌\",\"BirthPlace\":\"内蒙古\",\"Sex\":\"男\"},{\"Count\":9,\"Id\":\"f2b0edce5da430fe\",\"IdCard\":\"1502021966****2117\",\"FactName\":\"刘斌\",\"BirthPlace\":\"内蒙古\",\"Sex\":\"男\"},{\"Count\":10,\"Id\":\"06366386ad6f47ee\",\"IdCard\":\"150204********1816\",\"FactName\":\"刘斌\",\"BirthPlace\":\"内蒙古******\",\"Sex\":\"男\"}]}";
       String sourceText = "";
        PlainText json = new Json(sourceText);
        Selectable selectables = json.jsonPath("$.Items");
        for(Selectable selectable :selectables.nodes()) {
            json = new Json(selectable.get());
            selectable = json.jsonPath("$.Count");
            System.out.println(selectable.get());
        }
    }

    public void close() {
        try {
            httpClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getText(String redirectLocation) throws Exception {
        String content = null;
        try {
            HttpGet httpget = new HttpGet(redirectLocation);
            httpget.setHeaders(headers.toArray(new Header[headers.size()]));
            httpget.setConfig(requestConfig);
            response = httpClient.execute(httpget);
            printHeader(response.getAllHeaders());
            content = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
            content = null;
        }
        System.out.println(content);
        return content;
    }


    public static void pushWhenNoDuplicate(Map map) {
        queue.add(map);
    }

    public static synchronized Map poll() {
        return queue.poll();
    }

    public static int getLeftRequestsCount() {
        return queue.size();
    }

    public String postText(String redirectLocation, Map postData, Request request) throws Exception {
        String content = "";
        HttpPost httpost = new HttpPost(redirectLocation);
        httpost.setHeaders(headers.toArray(new Header[headers.size()]));
        httpost.setConfig(requestConfig);
        List<NameValuePair> nvps = getPostData(postData, request);
        httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
        response = httpClient.execute(httpost);
        printHeader(response.getAllHeaders());
        content = EntityUtils.toString(response.getEntity(), "UTF-8");
        System.out.println(content);
        return content;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void init() {
        RequestConfig defaultRequestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.BEST_MATCH)
                .setExpectContinueEnabled(true)
                .setStaleConnectionCheckEnabled(true)
                .setTargetPreferredAuthSchemes(Arrays.asList(AuthSchemes.NTLM, AuthSchemes.DIGEST))
                .setProxyPreferredAuthSchemes(Arrays.asList(AuthSchemes.BASIC))
                .build();
        requestConfig = RequestConfig.copy(defaultRequestConfig)
                .setSocketTimeout(500000)
                .setConnectTimeout(500000)
                .setConnectionRequestTimeout(500000)
                .setCircularRedirectsAllowed(true)
                .build();
        headers = new ArrayList<>();
        headers.add(new BasicHeader("User-Agent", "User-Agent: Mozilla/5.0 (Windows NT 10.0; WOW64; rv:42.0) Gecko/20100101 Firefox/42.0"));
    }

    public AuthenticationUtil() {
        init();
        HttpRequestRetryHandler myRetryHandler = new HttpRequestRetryHandler() {

            public boolean retryRequest(IOException exception, int executionCount, HttpContext httpContext) {
                if (executionCount >= 5) {
                    // 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof InterruptedIOException) {
                    // 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {
                    // 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {
                    // 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {
                    // ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(httpContext);
                HttpRequest request = clientContext.getRequest();
                boolean idempotent = !(request instanceof HttpEntityEnclosingRequest);
                if (idempotent) {
                    return true;
                }
                return false;
            }
        };
        httpClient = HttpClientBuilder.create().setRetryHandler(myRetryHandler).build();
    }

    public AuthenticationUtil(CloseableHttpClient httpClient) {
        init();
        this.httpClient = httpClient;
    }

    public static boolean getCheckCode(String content, String regex, StringBuffer img) {
        String[] parames = StringUtils.split(regex, ";");
        if (parames.length > 2) {
            img.append(parames[2]);
        }
        if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "regex")) {
            Pattern pattern = Pattern.compile(parames[0]);
            Matcher matcher = pattern.matcher(content);
            if (matcher.find()) {
                for (int i = 1; i <= matcher.groupCount(); i++) {
                    if (StringUtils.isNotEmpty(matcher.group(i))) {
                        img.append(matcher.group(i));
                        break;
                    }
                }
                return false;
            }
        } else if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "xpath")) {
            Html html = new Html(content);
            String temp = html.xpath(parames[0]).toString();
            if (StringUtils.isEmpty(temp)) {
                return true;
            } else if (parames.length == 2) {
                img.append(temp);
                return false;
            } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
                return true;
        } else if (parames.length > 1 && StringUtils.equalsIgnoreCase(parames[1], "jsonPath")) {
            Json json = new Json(content);
            String temp = json.jsonPath(parames[0]).toString();
            if (StringUtils.isEmpty(temp)) {
                return true;
            } else if (parames.length == 2) {
                img.append(temp);
                return false;
            } else if (!StringUtils.equalsIgnoreCase(temp, parames[2]))
                return true;
        }

        return true;
    }

    public void saveJpsg(String url, String path, int time) {
        try {
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
            File destF = new File(path);
            if (!destF.exists()) {
                destF.mkdirs();
            }
            for (int i = 0; i < 1000; i++) {

                HttpGet httpget = new HttpGet(url);
                headers.add(new BasicHeader("Accept", "image/webp,image/*,*/*;q=0.8"));
                httpget.setHeaders(headers.toArray(new Header[headers.size()]));
                httpget.setConfig(requestConfig);
                response = httpClient.execute(httpget);
                File storeFile = new File(path + "/" + i + ".jpg");
                FileOutputStream output = new FileOutputStream(storeFile);

// 得到网络资源的字节数组,并写入文件
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    InputStream instream = entity.getContent();

                    byte b[] = new byte[1024];
                    int j = 0;
                    while ((j = instream.read(b)) != -1) {
                        output.write(b, 0, j);
                    }
                    output.flush();
                    output.close();
                }
                Thread.sleep(time);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveExe(String url, String name) {
        try {
            //Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + img);
            String path = System.getProperty("user.dir");
            File destF = new File(path);
            if (!destF.exists()) {
                destF.mkdirs();
            }

            HttpGet httpget = new HttpGet(url);
            headers.add(new BasicHeader("Accept", "image/webp,image/*,*/*;q=0.8"));
            httpget.setHeaders(headers.toArray(new Header[headers.size()]));
            httpget.setConfig(requestConfig);
            response = httpClient.execute(httpget);
            File storeFile = new File(path + "/" + name);
            FileOutputStream output = new FileOutputStream(storeFile);
// 得到网络资源的字节数组,并写入文件
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();

                byte b[] = new byte[1024];
                int j = 0;
                while ((j = instream.read(b)) != -1) {
                    output.write(b, 0, j);
                }
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return "success";
    }

    private static String tesseract() throws Exception {
        FileInputStream fin = new FileInputStream(new File("D:/testdata/1.jpg"));
        BufferedImage bi = ImageIO.read(fin);
        Vaildcode flt = new Vaildcode(bi);
        flt.fixBackground();    //去除底色
        flt.changeGrey();   //二值化，单色化

        //flt.filteNoise();   //第二次去除干扰
        //flt.getGrey();    //转换为灰度
        //flt.getBrighten();
        /*
        int w = 1;
        while(flt.findWord(0,"C:\\Users\\max\\Pictures\\word"+w+".bmp")){
            w++;
        }
        */

        //flt.getRotate(-30);
        //bi=flt.getProcessedImg();
        flt.saveFile(flt.getProcessedImg(), "d:/testdata/1.jpg", "bmp");
        String recognizeText = new OCRHelper().recognizeText(new File("d:/testdata/1.bmp"));
        return recognizeText;
    }

    public static List<NameValuePair> getPostData(Map<String, Object> map, Request request) {
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        Map table = null;
        if (request != null) {
            table = (HashMap) request.getExtra("$table");
        }
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() != null && StringUtils.startsWith(entry.getValue().toString(), "$UCSMY_"))
                nvps.add(new BasicNameValuePair(entry.getKey(), table.get(entry.getKey().toString()).toString()));
            else
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
        }
        return nvps;
    }

    private void printHeader(Header[] headers) {
        for (int i = 0; i < headers.length; i++) {
            System.out.println(headers[i].getName() + "-----===----" + headers[i].getValue());
        }
    }

    public static List<Header> getHeaders() {
        return headers;
    }

    public static void setHeaders(List<Header> headers) {
        AuthenticationUtil.headers = headers;
    }

}
