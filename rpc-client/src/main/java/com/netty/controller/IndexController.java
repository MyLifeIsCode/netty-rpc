package com.netty.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netty.entity.InfoUser;
import com.netty.service.InfoUserService;
import com.netty.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@RequestMapping(value = "/")
@RestController
public class IndexController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private InfoUserService userService;

    @RequestMapping("/insert")
    @ResponseBody
    public List<InfoUser> getUserList() throws InterruptedException {
        long start = System.currentTimeMillis();
        int thread_count = 1;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                InfoUser infoUser = new InfoUser(IdUtil.getId(),"Jeen","BeiJing");
                List<InfoUser> users = userService.insertInfoUser(infoUser);
                logger.info("返回用户信息记录:{}", JSON.toJSONString(users));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));
        return null;
    }
    @RequestMapping("getAllUser")
    @ResponseBody
    public Map<String, InfoUser> getAllUser() throws InterruptedException {

        long start = System.currentTimeMillis();
        int thread_count = 1000;
        CountDownLatch countDownLatch = new CountDownLatch(thread_count);
        for (int i=0;i<thread_count;i++){
            new Thread(() -> {
                Map<String, InfoUser> allUser = userService.getAllUser();
                logger.info("查询所有用户信息：{}", JSONObject.toJSONString(allUser));
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        long end = System.currentTimeMillis();
        logger.info("线程数：{},执行时间:{}",thread_count,(end-start));

        return null;
    }
}
