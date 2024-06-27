package com.jia.admin.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.jia.admin.common.database.BaseDO;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_user")
public class UserDO extends BaseDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String mail;

    /**
     * 注销时间戳
     */
    private Long deletionTime;



    public UserDO() {}
}
