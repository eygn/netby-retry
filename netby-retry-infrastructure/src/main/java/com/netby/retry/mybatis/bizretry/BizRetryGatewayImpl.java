package com.netby.retry.mybatis.bizretry;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.netby.common.vo.BaseVO;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Elvan.bai
 * @date 2023/7/16 10:05
 */
@Service
@RequiredArgsConstructor
public class BizRetryGatewayImpl implements BizRetryGateway {

    private final BizRetryMapper bizRetryMapper;

    @Override
    public BizRetry getBizRetry(String retryId) {
        return BaseVO.copyTo(bizRetryMapper.selectById(retryId), BizRetry.class);
    }

    @Override
    public List<BizRetry> listByBizType(String bizType) {
        List<BizRetryDO> list = bizRetryMapper.selectList(new LambdaQueryWrapper<>(BizRetryDO.class).eq(BizRetryDO::getBizType, bizType));
        return BaseVO.copyListTo(list, BizRetry.class);
    }

    @Override
    public boolean update(BizRetry bizRetry) {
        return bizRetryMapper.updateById(bizRetry.copyTo(BizRetryDO.class)) == 1;
    }

    @Override
    public boolean add(BizRetry bizRetry) {
        BizRetryDO bizRetryDO = bizRetry.copyTo(BizRetryDO.class);
        bizRetryMapper.insert(bizRetryDO);
        return true;
    }
}
