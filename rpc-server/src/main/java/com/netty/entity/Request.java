package com.netty.entity;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author qll
 * @create 2019-02-27 12:12
 * @desc request
 **/
@Data
@ToString
public class Request implements Serializable {
    private String id;
    private String className;// 类名
    private String methodName;// 函数名称
    private Class<?>[] parameterTypes;// 参数类型
    private Object[] parameters;// 参数列表
}
