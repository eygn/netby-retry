package com.netby.retry.mybatis.bizretry;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.netby.common.vo.BaseVO;
import com.netby.common.vo.PageResult;
import com.netby.common.vo.QueryPage;
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
    public PageResult<BizRetry> queryList(String bizType, QueryPage queryPage) {
        LambdaQueryWrapper queryWrapper = new LambdaQueryWrapper<>(BizRetryDO.class).eq(BizRetryDO::getBizType, bizType);
        Page<BizRetryDO> page = bizRetryMapper.selectPage(new Page<>(queryPage.getPageNo(), queryPage.getPageSize()), queryWrapper);
        PageResult<BizRetry> pageResult = new PageResult<>();
        pageResult.setCurrentPage(page.getCurrent());
        pageResult.setPages(page.getPages());
        pageResult.setRecords(BaseVO.copyListTo(page.getRecords(), BizRetry.class));
        pageResult.setTotalCount(bizRetryMapper.selectCount(queryWrapper));
        return pageResult;
    }

    @Override
    public boolean update(BizRetry bizRetry) {
        return bizRetryMapper.updateById(bizRetry.copyTo(BizRetryDO.class)) == 1;
    }

    @Override
    public BizRetry add(BizRetry bizRetry) {
        BizRetryDO bizRetryDO = bizRetry.copyTo(BizRetryDO.class);
        bizRetryMapper.insert(bizRetryDO);
        return BaseVO.copyTo(bizRetryDO, BizRetry.class);
    }
}
