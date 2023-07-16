package com.netby.retry.executor;

import com.netby.common.vo.PageResult;
import com.netby.retry.domain.retry.BizRetry;
import com.netby.retry.domain.retry.gateway.BizRetryGateway;
import com.netby.retry.dto.BizRetryListQuery;
import com.netby.retry.dto.data.BizRetryDTO;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @author Elvan.bai
 * @date 2023/7/16 10:08
 */
@Component
@RequiredArgsConstructor
public class BizRetryListQueryExe {

    @Getter
    private final BizRetryGateway bizRetryGateway;

    public PageResult<BizRetryDTO> execute(BizRetryListQuery cmd) {
        PageResult<BizRetry> bizRetryPageResult = bizRetryGateway.queryList(cmd.getBizType(), cmd);
        return PageResult.of(bizRetryPageResult, BizRetryDTO.class);
    }
}
