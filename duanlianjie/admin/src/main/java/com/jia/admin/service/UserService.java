package com.jia.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.admin.dao.entity.UserDO;
import com.jia.admin.dto.req.UserLoginReqDTO;
import com.jia.admin.dto.req.UserRegisterReqDTO;
import com.jia.admin.dto.req.UserUpdateReqDTO;
import com.jia.admin.dto.resp.UserLoginRespDTO;
import com.jia.admin.dto.resp.UserRespDTO;


public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);

    Boolean hasUsername(String username);

    void register(UserRegisterReqDTO requestParam);


    void update(UserUpdateReqDTO requestParam);

    UserLoginRespDTO login(UserLoginReqDTO requestParam);

    Boolean checkLogin(String username, String token);
}
