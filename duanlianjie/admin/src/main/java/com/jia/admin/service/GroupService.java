package com.jia.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jia.admin.dao.entity.GroupDO;
import com.jia.admin.dto.req.ShortLinkGroupSortReqDTO;
import com.jia.admin.dto.req.ShortLinkGroupUpdateReqDTO;
import com.jia.admin.dto.resp.ShortLinkGroupRespDTO;

import java.util.List;

public interface GroupService extends IService<GroupDO> {
    void saveGroup(String groupName);

    List<ShortLinkGroupRespDTO> listGroup();

    void updateGroup(ShortLinkGroupUpdateReqDTO requestParam);

    void deleteGroup(String gid);

    void sortGroup(List<ShortLinkGroupSortReqDTO> requestParam);
}
