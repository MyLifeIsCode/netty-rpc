package com.netty.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.netty.annotation.RpcService;
import com.netty.entity.InfoUser;
import com.netty.service.InfoUserService;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * @author qll
 * @create 2019-02-27 12:09
 * @desc 实现类
 **/
@Slf4j
@RpcService
public class InfoUserServiceImpl implements InfoUserService {

    //当做数据库，存储用户信息
    Map<String,InfoUser> infoUserMap = new HashMap<>();
    public List<InfoUser> insertInfoUser(InfoUser infoUser) {
        log.info("新增用户信息:{}", JSONObject.toJSONString(infoUser));
        infoUserMap.put(infoUser.getId(),infoUser);
        return getInfoUserList();
    }
    public InfoUser getInfoUserById(String id) {
        InfoUser infoUser = infoUserMap.get(id);
        log.info("查询用户ID:{}",id);
        return infoUser;
    }

    public List<InfoUser> getInfoUserList() {
        List<InfoUser> userList = new ArrayList<>();
        Iterator<Map.Entry<String, InfoUser>> iterator = infoUserMap.entrySet().iterator();
        while (iterator.hasNext()){
            Map.Entry<String, InfoUser> next = iterator.next();
            userList.add(next.getValue());
        }
        log.info("返回用户信息记录数:{}",userList.size());
        return userList;
    }
    public void deleteInfoUserById(String id) {
        log.info("删除用户信息:{}",JSONObject.toJSONString(infoUserMap.remove(id)));
    }
    public String getNameById(String id){
        log.info("根据ID查询用户名称:{}",id);
        return infoUserMap.get(id).getName();
    }
    public Map<String,InfoUser> getAllUser(){
        log.info("查询所有用户信息{}",infoUserMap.keySet().size());
        return infoUserMap;
    }
}
