package com.jia.project.service.Impl;

import cn.hutool.core.text.StrBuilder;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jia.project.common.convention.exception.ServiceException;
import com.jia.project.dao.entity.ShortLinkDO;
import com.jia.project.dao.mapper.ShortLinkMapper;
import com.jia.project.dto.req.ShortLinkCreateReqDTO;
import com.jia.project.dto.resp.ShortLinkCreateRespDTO;
import com.jia.project.service.ShortLinkService;
import com.jia.project.toolkit.HashUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBloomFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShortLinkServiceImpl extends ServiceImpl<ShortLinkMapper, ShortLinkDO> implements ShortLinkService {

    private final RBloomFilter<String> shortUriCreateCachePenetrationBloomFilter;

    @Value("${short-link.domain.default}")
    private  String defaultDomain;

    @Override
    public ShortLinkCreateRespDTO createShortLink(ShortLinkCreateReqDTO requestParam) {
        String shortLinkSuffix = generateSuffix(requestParam);
        String fullShortLink = StrBuilder.create(defaultDomain)
                .append("/")
                .append(shortLinkSuffix)
                .toString();
        ShortLinkDO shortLinkDO = ShortLinkDO.builder()
                .domain(defaultDomain)
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .createdType(requestParam.getCreatedType())
                .validDate(requestParam.getValidDate())
                .validDateType(requestParam.getValidDateType())
                .describe(requestParam.getDescribe())
                .shortUri(shortLinkSuffix)
                .enableStatus(0)
                .fullShortUrl(fullShortLink)
                .build();
        try {
            baseMapper.insert(shortLinkDO);//唯一约束的问题
        }catch (DuplicateKeyException e){
            LambdaQueryWrapper<ShortLinkDO> query = Wrappers.lambdaQuery(ShortLinkDO.class)
                    .eq(ShortLinkDO::getFullShortUrl, fullShortLink);
            ShortLinkDO hasShortLink = baseMapper.selectOne(query);
            if(hasShortLink != null){
                log.warn("短链接：{}重复入库",fullShortLink);
            }
            // 首先判断是否存在布隆过滤器，如果不存在直接新增
            if (!shortUriCreateCachePenetrationBloomFilter.contains(fullShortLink)) {
                shortUriCreateCachePenetrationBloomFilter.add(fullShortLink);
            }
            throw new ServiceException(String.format("短链接：%s 生成重复", fullShortLink));
        }
        //存入布隆过滤器
        shortUriCreateCachePenetrationBloomFilter.add(shortLinkSuffix);
        return ShortLinkCreateRespDTO.builder()
                .fullShortUrl(shortLinkDO.getFullShortUrl())
                .originUrl(requestParam.getOriginUrl())
                .gid(requestParam.getGid())
                .build();
    }

    private String generateSuffix(ShortLinkCreateReqDTO requestParam) {
        //因为短链接只是有6位所以很容易重复，判断是否重复就要查询数据库，压力很大就是部署布隆过滤器
        int customGenerateCount = 0;
        String shortLink;
        while (true){
            if(customGenerateCount > 10){//最多重试10次
                throw new ServiceException("短链接频繁生成，请稍后再尝试");
            }
            String originUrl = requestParam.getOriginUrl();
            originUrl += UUID.randomUUID().toString();//减少哈希冲突
            shortLink = HashUtil.hashToBase62(originUrl);
            if(!shortUriCreateCachePenetrationBloomFilter.contains(defaultDomain+shortLink)){
                break;
            }
            customGenerateCount++;
        }
        return HashUtil.hashToBase62(shortLink);
    }

}
