package com.netty.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author qll
 * @create 2019-02-27 12:21
 * @desc response
 **/
@Data
public class Response implements Serializable {
    private String requestId;
    private int code;
    private String error_msg;
    private Object data;
}
