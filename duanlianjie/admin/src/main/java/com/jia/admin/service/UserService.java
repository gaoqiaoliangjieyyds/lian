package com.jia.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.admin.dao.entity.UserDO;
import com.jia.admin.dto.resp.UserRespDTO;


public interface UserService extends IService<UserDO> {
    UserRespDTO getUserByUsername(String username);
}
