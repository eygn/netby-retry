package com.netby.retry.biz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.netby.retry.biz.enums.RetryStatusEnum;
import com.netby.retry.biz.mapper.BizRetryMapper;
import com.netby.retry.biz.entity.BizRetryDO;
import com.netby.retry.biz.service.BizRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 服务重试表(BizRetry)表服务实现类
 *
 * @author: byg
 * @since 2023-06-17 17:09:52
 */
@Slf4j
@Service("bizRetryService")
public class BizRetryServiceImpl extends ServiceImpl<BizRetryMapper, BizRetryDO> implements BizRetryService {

    @Resource
    private BizRetryMapper bizRetryMapper;

    @Override
    public void updateRetry(BizRetryDO riskDecisionRetryDO, RetryStatusEnum retryStatusEnum) {
        BizRetryDO existDo = bizRetryMapper.selectOne(new LambdaQueryWrapper<BizRetryDO>()
                .eq(BizRetryDO::getBizNo, riskDecisionRetryDO.getBizNo())
                .eq(BizRetryDO::getBizType, riskDecisionRetryDO.getBizType()));
        if (existDo != null) {
            existDo.setRetryCount(existDo.getRetryCount() + 1);
            existDo.setRetryStatus(retryStatusEnum.getCode());
            existDo.setComment(riskDecisionRetryDO.getComment());
            bizRetryMapper.updateById(existDo);
            riskDecisionRetryDO.setRetryStatus(existDo.getRetryStatus());
            return;
        }
        log.error("未匹配到重试记录,bizNo:{}", riskDecisionRetryDO.getBizNo());
    }

}

