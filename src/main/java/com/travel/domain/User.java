package com.travel.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 用户实体类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor  //满参构造必须要创建无参构造，否则会报错
public class User implements Serializable {

    private int uid;//用户id
    private String username;//用户名，账号
    private String password;//密码
    private String telephone;//手机号
    private String salt; // 盐

}
