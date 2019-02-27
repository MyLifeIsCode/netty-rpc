package com.netty.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qll
 * @create 2019-02-27 11:33
 * @desc 用户信息
 **/
@Data
public class InfoUser implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String id;

    private String name;

    private String address;
}
