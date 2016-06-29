package com.ucsmy.collection.controllers;

import com.ucsmy.collection.dto.Link;
import com.ucsmy.collection.models.Spider;
import com.ucsmy.collection.services.SpiderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Bill on 2015/11/15.
 */
@Controller
@RequestMapping("spider")
public class SpiderController {

    @Autowired
    private SpiderService spiderService;

    @ResponseBody
    @RequestMapping("/regedit/{spiderId}/{jobId}/{count}")
    public Map regeditSpider(@PathVariable String spiderId,@PathVariable Long jobId,@PathVariable Integer count, HttpServletRequest request) {
        System.out.println(spiderId);
        String agentID = request.getParameter("agentID");
        String info = request.getParameter("info");
        String mac = request.getParameter("mac");
        String version = request.getParameter("version");
        Map map = spiderService.regedit(spiderId,jobId,info,request,version,agentID,mac,count);
        return map;
    }

    @ResponseBody
    @RequestMapping("/regedit")
    public Map regeditSpider(HttpServletRequest request) {
        String agentID = request.getParameter("agentID");
        String info = request.getParameter("info");
        String mac = request.getParameter("mac");
        String version = request.getParameter("version");
        Map map = spiderService.regedit(null,null,info,request,version,agentID,mac,null);
        return map;
    }

    @ResponseBody
    @RequestMapping("/listAjax")
    public List<Spider> getSpiders() {
        return spiderService.getAllSpider();
    }

    @ResponseBody
    @RequestMapping("/save")
    public Map saveCollectionData(HttpServletRequest request,HttpServletResponse response) {
        String content = request.getParameter("content");

        Map map = spiderService.saveCollectionData(content);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getlinks/{linkname}/{length}")
    public Link getLinks(@PathVariable String linkname,@PathVariable Long length) {
        Link links = spiderService.getlinks(linkname,length);
        return links;
    }

    @ResponseBody
    @RequestMapping("/getcaptcha/{language}")
    public Map getCaptcha(@PathVariable String language, @RequestParam("captcha") MultipartFile captcha) {
        Map map = spiderService.getCaptcha(language,captcha);
        return map;
    }

    @ResponseBody
    @RequestMapping("/getVersion")
    public Map getVersion(double version) {
        Map map = spiderService.getVersion(version);
        return map;
    }

    @RequestMapping("/download")
    public String download(String fileName, HttpServletRequest request,
                           HttpServletResponse response) {
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName="
                + fileName);
        spiderService.downlod(fileName,request,response);
        //  返回值要注意，要不然就出现下面这句错误！
        //java+getOutputStream() has already been called for this response
        return null;
    }

    @ResponseBody
    @RequestMapping("/getFinancial")
    public Map getFinancial(String reqTime,String companyCode,HttpServletRequest request,
                            HttpServletResponse response) {
        Map map = spiderService.getFinancial(reqTime,companyCode);
        return map;
    }

}
