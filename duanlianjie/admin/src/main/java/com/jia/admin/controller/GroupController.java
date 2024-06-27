package com.jia.admin.controller;

import com.jia.admin.common.convention.result.Result;
import com.jia.admin.common.convention.result.Results;
import com.jia.admin.dto.req.ShortLinkGroupSaveReqDTO;
import com.jia.admin.dto.resp.ShortLinkGroupRespDTO;
import com.jia.admin.service.GroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    /**
     * 新增短链接分组
     */
    @PostMapping("/api/short-link/admin/v1/group")
    public Result<Void> save(@RequestBody ShortLinkGroupSaveReqDTO requestParam) {
        groupService.saveGroup(requestParam.getGroupName());
        return Results.success();
    }


    /**
     * 查询短链接分组集合
     */
    @GetMapping("/api/short-link/admin/v1/group")
    public Result<List<ShortLinkGroupRespDTO>> listGroup() {
        return Results.success(groupService.listGroup());
    }

}
