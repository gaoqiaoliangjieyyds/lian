package com.jia.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.admin.dao.entity.GroupDO;

public interface GroupService extends IService<GroupDO> {
    void saveGroup(String groupName);
}
