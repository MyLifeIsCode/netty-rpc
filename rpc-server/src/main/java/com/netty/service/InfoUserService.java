package com.netty.service;

import com.netty.entity.InfoUser;

import java.util.List;
import java.util.Map;

/**
 * @author qll
 * @create 2019-02-27 12:04
 * @desc service
 **/

public interface InfoUserService {
    List<InfoUser> insertInfoUser(InfoUser infoUser);
    InfoUser getInfoUserById(String id);
    void deleteInfoUserById(String id);
    String getNameById(String id);
    Map<String,InfoUser> getAllUser();
}
