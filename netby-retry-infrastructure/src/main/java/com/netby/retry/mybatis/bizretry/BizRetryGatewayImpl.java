package com.netby.retry.mybatis.bizretry;

import com.netby.common.vo.BaseVO;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BizRetryGatewayImpl implements BizRetryGateway {

    @Getter
    private final BizRetryMapper bizRetryMapper;

    @Override
    public BizRetry getBizRetry(String retryId) {
        return BaseVO.copyTo(bizRetryMapper.selectById(retryId), BizRetry.class);
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
